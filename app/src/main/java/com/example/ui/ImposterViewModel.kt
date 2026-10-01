package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WordRepository
import com.example.model.GamePhase
import com.example.model.GameState
import com.example.model.Player
import com.example.model.WordItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.SecureRandom

class ImposterViewModel(application: Application) : AndroidViewModel(application) {

  private val secureRandom = SecureRandom()
  private var timerJob: Job? = null
  private var dealingJob: Job? = null

  private val defaultPlayerNames = listOf(
    "Jordan", "Sam", "Alex", "Taylor", "Morgan", "Riley", "Casey", "Avery"
  )

  private val _gameState = MutableStateFlow(
    GameState(
      phase = GamePhase.LANDING,
      players = listOf(
        Player(name = "Jordan", avatarColorIndex = 0),
        Player(name = "Sam", avatarColorIndex = 1),
        Player(name = "Alex", avatarColorIndex = 2),
        Player(name = "Taylor", avatarColorIndex = 3)
      )
    )
  )
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  fun goToLobby() {
    _gameState.update { it.copy(phase = GamePhase.LOBBY) }
  }

  fun goToLanding() {
    timerJob?.cancel()
    dealingJob?.cancel()
    _gameState.update { it.copy(phase = GamePhase.LANDING) }
  }

  fun updatePlayerName(playerId: String, newName: String) {
    val trimmed = newName.trim()
    if (trimmed.isEmpty()) return
    _gameState.update { state ->
      val updated = state.players.map { player ->
        if (player.id == playerId) player.copy(name = trimmed) else player
      }
      state.copy(players = updated)
    }
  }

  fun clearAllPlayers() {
    _gameState.update { it.copy(players = emptyList()) }
  }

  fun addPlayer(name: String) {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return
    val current = _gameState.value.players
    if (current.size >= 20) return // Up to 20 players supported!
    if (current.any { it.name.equals(trimmed, ignoreCase = true) }) return
    val newIndex = current.size % 8
    val newPlayer = Player(name = trimmed, avatarColorIndex = newIndex)
    _gameState.update { it.copy(players = current + newPlayer) }
  }

  fun removePlayer(playerId: String) {
    _gameState.update { state ->
      val updated = state.players.filterNot { it.id == playerId }
      state.copy(players = updated)
    }
  }

  fun setQuickPlayers(count: Int) {
    val targetCount = count.coerceIn(3, 20)
    val names = if (targetCount <= defaultPlayerNames.size) {
      defaultPlayerNames.take(targetCount)
    } else {
      defaultPlayerNames + (defaultPlayerNames.size + 1..targetCount).map { "Player $it" }
    }
    val players = names.mapIndexed { index, name ->
      Player(name = name, avatarColorIndex = index % 8)
    }
    _gameState.update { it.copy(players = players) }
  }

  fun toggleCategory(category: String) {
    _gameState.update { state ->
      val currentSelected = state.selectedCategories.toMutableSet()
      if (currentSelected.contains(category)) {
        currentSelected.remove(category)
      } else {
        currentSelected.add(category)
      }
      state.copy(selectedCategories = currentSelected)
    }
  }

  fun setTimerDuration(seconds: Int) {
    _gameState.update { it.copy(timerDurationSeconds = seconds, discussionSecondsRemaining = seconds) }
  }

  fun setImposterCount(count: Int) {
    _gameState.update { it.copy(imposterCount = count) }
  }

  fun startGame() {
    val players = _gameState.value.players
    if (players.size < 3) return

    val selectedCategories = _gameState.value.selectedCategories
    val (word, hint) = WordRepository.getRandomWord(selectedCategories)

    // Mathematically uniform cryptographic selection
    // Every single player has equal 1/N chance of being selected as imposter
    val shuffledPlayers = players.shuffled(secureRandom)

    val maxImposters = ((players.size - 1) / 2).coerceAtLeast(1)
    val imposterCount = _gameState.value.imposterCount.coerceIn(1, maxImposters)
    val chosenImposterIds = shuffledPlayers.take(imposterCount).map { it.id }.toSet()

    // Pass-and-play order is also randomly scrambled
    val passOrder = shuffledPlayers.shuffled(secureRandom).map { p ->
      p.copy(
        isImposter = chosenImposterIds.contains(p.id),
        roleSeen = false,
        votesReceived = 0
      )
    }

    val firstSpeakerIndex = secureRandom.nextInt(passOrder.size)

    _gameState.update {
      it.copy(
        phase = GamePhase.ROLE_DEALING,
        currentWord = word,
        activeHint = hint,
        imposterIds = chosenImposterIds,
        shuffledOrder = passOrder,
        currentPassIndex = 0,
        isSecretVisible = false,
        activeSpeakerIndex = firstSpeakerIndex,
        discussionSecondsRemaining = it.timerDurationSeconds,
        isTimerRunning = false,
        accusedPlayerId = null,
        imposterGuessResult = null,
        roundWinnerMessage = ""
      )
    }

    // Quick subtle transition into pass-reveal phase
    dealingJob?.cancel()
    dealingJob = viewModelScope.launch {
      delay(400)
      _gameState.update { it.copy(phase = GamePhase.PASS_REVEAL) }
    }
  }

  fun setSecretVisibility(visible: Boolean) {
    _gameState.update { state ->
      if (visible) {
        val currentIdx = state.currentPassIndex
        val updatedOrder = state.shuffledOrder.mapIndexed { idx, player ->
          if (idx == currentIdx) player.copy(roleSeen = true) else player
        }
        state.copy(isSecretVisible = true, shuffledOrder = updatedOrder)
      } else {
        state.copy(isSecretVisible = false)
      }
    }
  }

  fun nextPassPlayer() {
    _gameState.update { state ->
      val nextIdx = state.currentPassIndex + 1
      if (nextIdx < state.shuffledOrder.size) {
        state.copy(currentPassIndex = nextIdx, isSecretVisible = false)
      } else {
        state.copy(
          phase = GamePhase.DISCUSSION,
          isSecretVisible = false,
          discussionSecondsRemaining = state.timerDurationSeconds,
          isTimerRunning = true
        )
      }
    }
    if (_gameState.value.phase == GamePhase.DISCUSSION) {
      startDiscussionTimer()
    }
  }

  fun startDiscussionTimer() {
    timerJob?.cancel()
    _gameState.update { it.copy(isTimerRunning = true) }
    timerJob = viewModelScope.launch {
      while (_gameState.value.discussionSecondsRemaining > 0 && _gameState.value.isTimerRunning) {
        delay(1000)
        _gameState.update {
          val remaining = it.discussionSecondsRemaining - 1
          it.copy(discussionSecondsRemaining = remaining)
        }
      }
      _gameState.update { it.copy(isTimerRunning = false) }
    }
  }

  fun pauseDiscussionTimer() {
    timerJob?.cancel()
    _gameState.update { it.copy(isTimerRunning = false) }
  }

  fun addTimeToTimer(seconds: Int = 30) {
    _gameState.update {
      it.copy(discussionSecondsRemaining = it.discussionSecondsRemaining + seconds)
    }
    if (!_gameState.value.isTimerRunning) {
      startDiscussionTimer()
    }
  }

  fun nextSpeaker() {
    _gameState.update { state ->
      val count = state.shuffledOrder.size
      if (count == 0) state
      else state.copy(activeSpeakerIndex = (state.activeSpeakerIndex + 1) % count)
    }
  }

  fun proceedToVoting() {
    timerJob?.cancel()
    _gameState.update { it.copy(phase = GamePhase.VOTING, isTimerRunning = false) }
  }

  fun castVoteForPlayer(targetPlayerId: String) {
    _gameState.update { state ->
      val updated = state.shuffledOrder.map { player ->
        if (player.id == targetPlayerId) {
          player.copy(votesReceived = player.votesReceived + 1)
        } else player
      }
      state.copy(shuffledOrder = updated)
    }
  }

  fun resetVotes() {
    _gameState.update { state ->
      val reset = state.shuffledOrder.map { it.copy(votesReceived = 0) }
      state.copy(shuffledOrder = reset)
    }
  }

  fun accusePlayer(playerId: String) {
    _gameState.update {
      it.copy(
        accusedPlayerId = playerId,
        phase = GamePhase.REVEAL_SUSPENSE
      )
    }
  }

  fun confirmAccusationReveal() {
    val state = _gameState.value
    val accused = state.shuffledOrder.find { it.id == state.accusedPlayerId }
    val isActuallyImposter = accused?.isImposter == true

    if (isActuallyImposter) {
      _gameState.update {
        it.copy(
          phase = GamePhase.IMPOSTER_GUESS,
          innocentsWon = true
        )
      }
    } else {
      val imposterPlayer = state.shuffledOrder.find { it.isImposter }
      val imposterName = imposterPlayer?.name ?: "The Imposter"
      val accusedName = accused?.name ?: "The accused"

      updatePlayerScore(imposterPlayer?.id, +3)

      _gameState.update {
        it.copy(
          phase = GamePhase.ROUND_SUMMARY,
          innocentsWon = false,
          imposterGuessResult = null,
          roundWinnerMessage = "$accusedName was INNOCENT! $imposterName bluffed everyone and escapes undetected!"
        )
      }
    }
  }

  fun submitImposterGuess(guessedWord: String) {
    val state = _gameState.value
    val secretWord = state.currentWord?.word ?: ""
    val isCorrect = guessedWord.equals(secretWord, ignoreCase = true)

    val imposter = state.shuffledOrder.find { it.isImposter }
    val imposterName = imposter?.name ?: "The Imposter"

    if (isCorrect) {
      updatePlayerScore(imposter?.id, +3)
      _gameState.update {
        it.copy(
          phase = GamePhase.ROUND_SUMMARY,
          innocentsWon = false,
          imposterGuessResult = true,
          roundWinnerMessage = "$imposterName guessed the secret word '$secretWord' and stole the win!"
        )
      }
    } else {
      state.shuffledOrder.filterNot { it.isImposter }.forEach { innocent ->
        updatePlayerScore(innocent.id, +2)
      }
      _gameState.update {
        it.copy(
          phase = GamePhase.ROUND_SUMMARY,
          innocentsWon = true,
          imposterGuessResult = false,
          roundWinnerMessage = "Detectives win! $imposterName was caught and guessed '$guessedWord' instead of '$secretWord'!"
        )
      }
    }
  }

  private fun updatePlayerScore(playerId: String?, delta: Int) {
    if (playerId == null) return
    _gameState.update { state ->
      val updatedPlayers = state.players.map { player ->
        if (player.id == playerId) player.copy(score = player.score + delta) else player
      }
      val updatedShuffled = state.shuffledOrder.map { player ->
        if (player.id == playerId) player.copy(score = player.score + delta) else player
      }
      state.copy(players = updatedPlayers, shuffledOrder = updatedShuffled)
    }
  }

  fun playNextRound() {
    timerJob?.cancel()
    _gameState.update {
      it.copy(roundNumber = it.roundNumber + 1)
    }
    startGame()
  }

  fun returnToLobby() {
    timerJob?.cancel()
    dealingJob?.cancel()
    _gameState.update {
      it.copy(
        phase = GamePhase.LOBBY,
        currentPassIndex = 0,
        isSecretVisible = false,
        isTimerRunning = false,
        accusedPlayerId = null,
        imposterGuessResult = null
      )
    }
  }

  fun addCustomWord(word: String, category: String, hint: String, distractors: List<String>) {
    WordRepository.addCustomWord(word, category, hint, distractors)
  }
}
