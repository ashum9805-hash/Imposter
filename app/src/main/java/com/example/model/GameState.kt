package com.example.model

enum class GamePhase {
  LANDING,
  LOBBY,
  ROLE_DEALING,
  PASS_REVEAL,
  DISCUSSION,
  VOTING,
  REVEAL_SUSPENSE,
  IMPOSTER_GUESS,
  ROUND_SUMMARY
}

data class GameState(
  val phase: GamePhase = GamePhase.LANDING,
  val players: List<Player> = emptyList(),
  val shuffledOrder: List<Player> = emptyList(), // randomized sequence for pass-and-play
  val currentPassIndex: Int = 0,
  val isSecretVisible: Boolean = false,
  val currentWord: WordItem? = null,
  val activeHint: String = "",
  val imposterIds: Set<String> = emptySet(),
  val roundNumber: Int = 1,
  val activeSpeakerIndex: Int = 0,
  val discussionSecondsRemaining: Int = 90,
  val isTimerRunning: Boolean = false,
  val timerDurationSeconds: Int = 90,
  val selectedCategories: Set<String> = emptySet(),
  val imposterCount: Int = 1,
  val accusedPlayerId: String? = null,
  val imposterGuessResult: Boolean? = null,
  val roundWinnerMessage: String = "",
  val innocentsWon: Boolean = false
)
