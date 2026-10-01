package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.WordRepository
import com.example.model.GamePhase
import com.example.ui.ImposterViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Imposter", appName)
  }

  @Test
  fun `test landing page is initial phase and navigation works`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = ImposterViewModel(app)

    assertEquals(GamePhase.LANDING, viewModel.gameState.value.phase)
    viewModel.goToLobby()
    assertEquals(GamePhase.LOBBY, viewModel.gameState.value.phase)
    viewModel.goToLanding()
    assertEquals(GamePhase.LANDING, viewModel.gameState.value.phase)
  }

  @Test
  fun `test adding up to 20 players and removing players`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = ImposterViewModel(app)

    viewModel.setQuickPlayers(20)
    assertEquals(20, viewModel.gameState.value.players.size)

    // Cannot exceed 20
    viewModel.addPlayer("ExtraPlayer")
    assertEquals(20, viewModel.gameState.value.players.size)

    // Remove one player
    val firstId = viewModel.gameState.value.players.first().id
    viewModel.removePlayer(firstId)
    assertEquals(19, viewModel.gameState.value.players.size)

    // Edit a player's name
    val playerToEdit = viewModel.gameState.value.players.first()
    viewModel.updatePlayerName(playerToEdit.id, "CustomRenamed")
    val edited = viewModel.gameState.value.players.find { it.id == playerToEdit.id }
    assertEquals("CustomRenamed", edited?.name)

    // Clear all players
    viewModel.clearAllPlayers()
    assertEquals(0, viewModel.gameState.value.players.size)
  }

  @Test
  fun `test hints are short and concise`() {
    val words = WordRepository.getAllWords()
    assertTrue(words.isNotEmpty())
    words.forEach { wordItem ->
      wordItem.hints.forEach { hint ->
        val wordCount = hint.split("\\s+".toRegex()).size
        assertTrue(
          "Hint '$hint' for word '${wordItem.word}' should be short (1-3 words)",
          wordCount <= 4
        )
      }
    }

    val paper = words.find { it.word == "Paper" }
    assertNotNull(paper)
    assertTrue(paper!!.hints.contains("White"))

    val headphones = words.find { it.word == "Headphones" }
    assertNotNull(headphones)
    assertTrue(headphones!!.hints.contains("Music"))

    val umbrella = words.find { it.word == "Umbrella" }
    assertNotNull(umbrella)
    assertTrue(umbrella!!.hints.contains("Rain"))

    // Superheroes category verification
    assertTrue(WordRepository.CATEGORIES.contains("Superheroes"))
    val heroWords = words.filter { it.category == "Superheroes" }
    assertTrue(heroWords.size >= 8)
    val superman = heroWords.find { it.word == "Superman" }
    assertNotNull(superman)
    assertTrue(superman!!.hints.contains("Fly"))
  }

  @Test
  fun `test start game assigns exact imposter count with uniform chance and randomized pass order`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = ImposterViewModel(app)

    viewModel.setQuickPlayers(4)
    viewModel.setImposterCount(1)
    viewModel.startGame()

    val state = viewModel.gameState.value
    assertEquals(GamePhase.ROLE_DEALING, state.phase)
    assertNotNull(state.currentWord)
    assertTrue(state.activeHint.isNotBlank())
    assertEquals(1, state.imposterIds.size)
    assertEquals(4, state.shuffledOrder.size)

    val imposterPlayers = state.shuffledOrder.filter { it.isImposter }
    assertEquals(1, imposterPlayers.size)
    val detectivePlayers = state.shuffledOrder.filterNot { it.isImposter }
    assertEquals(3, detectivePlayers.size)
  }

  @Test
  fun `test imposter word guess steals the round`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = ImposterViewModel(app)

    viewModel.setQuickPlayers(4)
    viewModel.startGame()

    val word = viewModel.gameState.value.currentWord!!.word
    val imposter = viewModel.gameState.value.shuffledOrder.first { it.isImposter }

    // Accuse the imposter
    viewModel.accusePlayer(imposter.id)
    viewModel.confirmAccusationReveal()
    assertEquals(GamePhase.IMPOSTER_GUESS, viewModel.gameState.value.phase)

    // Imposter guesses correctly
    viewModel.submitImposterGuess(word)
    assertEquals(GamePhase.ROUND_SUMMARY, viewModel.gameState.value.phase)
    assertFalse(viewModel.gameState.value.innocentsWon)
    assertEquals(true, viewModel.gameState.value.imposterGuessResult)
  }
}
