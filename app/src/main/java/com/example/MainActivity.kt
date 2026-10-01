package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GamePhase
import com.example.ui.ImposterViewModel
import com.example.ui.screens.DiscussionScreen
import com.example.ui.screens.ImposterGuessScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.PassRevealScreen
import com.example.ui.screens.RoundSummaryScreen
import com.example.ui.screens.SuspenseScreen
import com.example.ui.screens.VotingScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.ImposterTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ImposterTheme {
        ImposterApp()
      }
    }
  }
}

@Composable
fun ImposterApp(
  viewModel: ImposterViewModel = viewModel()
) {
  val gameState by viewModel.gameState.collectAsStateWithLifecycle()

  // Handle Android system back gesture to navigate back gracefully
  BackHandler(enabled = gameState.phase != GamePhase.LANDING) {
    when (gameState.phase) {
      GamePhase.LOBBY -> viewModel.goToLanding()
      GamePhase.ROLE_DEALING,
      GamePhase.PASS_REVEAL -> viewModel.returnToLobby()
      GamePhase.DISCUSSION -> viewModel.returnToLobby()
      GamePhase.VOTING -> viewModel.startDiscussionTimer()
      GamePhase.REVEAL_SUSPENSE -> viewModel.proceedToVoting()
      GamePhase.IMPOSTER_GUESS -> viewModel.returnToLobby()
      GamePhase.ROUND_SUMMARY -> viewModel.returnToLobby()
      GamePhase.LANDING -> { /* Default exit */ }
    }
  }

  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    val contentModifier = Modifier
      .fillMaxSize()
      .padding(innerPadding)

    when (gameState.phase) {
      GamePhase.LANDING -> {
        LandingScreen(
          gameState = gameState,
          onStartPlaying = { viewModel.goToLobby() },
          onAddCustomWord = { word, category, hint, distractors ->
            viewModel.addCustomWord(word, category, hint, distractors)
          },
          modifier = contentModifier
        )
      }

      GamePhase.LOBBY -> {
        LobbyScreen(
          gameState = gameState,
          onAddPlayer = { viewModel.addPlayer(it) },
          onRemovePlayer = { viewModel.removePlayer(it) },
          onUpdatePlayerName = { id, newName -> viewModel.updatePlayerName(id, newName) },
          onClearAllPlayers = { viewModel.clearAllPlayers() },
          onSetQuickPlayers = { viewModel.setQuickPlayers(it) },
          onToggleCategory = { viewModel.toggleCategory(it) },
          onSetTimerDuration = { viewModel.setTimerDuration(it) },
          onSetImposterCount = { viewModel.setImposterCount(it) },
          onStartGame = { viewModel.startGame() },
          onBackToLanding = { viewModel.goToLanding() },
          onAddCustomWord = { word, category, hint, distractors ->
            viewModel.addCustomWord(word, category, hint, distractors)
          },
          modifier = contentModifier
        )
      }

      GamePhase.ROLE_DEALING -> {
        Box(
          contentAlignment = Alignment.Center,
          modifier = contentModifier.background(DarkBackground)
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            CircularProgressIndicator(
              color = NeonCyan,
              modifier = Modifier.size(52.dp),
              strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
              text = "Assigning Secret Roles...",
              color = NeonPurpleLight,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Pass the phone when ready",
              color = TextSecondary,
              fontSize = 13.sp
            )
          }
        }
      }

      GamePhase.PASS_REVEAL -> {
        PassRevealScreen(
          gameState = gameState,
          onHoldChange = { viewModel.setSecretVisibility(it) },
          onNextPlayer = { viewModel.nextPassPlayer() },
          modifier = contentModifier
        )
      }

      GamePhase.DISCUSSION -> {
        DiscussionScreen(
          gameState = gameState,
          onPauseResumeTimer = {
            if (gameState.isTimerRunning) viewModel.pauseDiscussionTimer()
            else viewModel.startDiscussionTimer()
          },
          onAdd30Seconds = { viewModel.addTimeToTimer(30) },
          onNextSpeaker = { viewModel.nextSpeaker() },
          onProceedToVoting = { viewModel.proceedToVoting() },
          modifier = contentModifier
        )
      }

      GamePhase.VOTING -> {
        VotingScreen(
          gameState = gameState,
          onCastVote = { viewModel.castVoteForPlayer(it) },
          onResetVotes = { viewModel.resetVotes() },
          onAccusePlayer = { viewModel.accusePlayer(it) },
          modifier = contentModifier
        )
      }

      GamePhase.REVEAL_SUSPENSE -> {
        SuspenseScreen(
          gameState = gameState,
          onRevealIdentity = { viewModel.confirmAccusationReveal() },
          modifier = contentModifier
        )
      }

      GamePhase.IMPOSTER_GUESS -> {
        ImposterGuessScreen(
          gameState = gameState,
          onSubmitGuess = { viewModel.submitImposterGuess(it) },
          modifier = contentModifier
        )
      }

      GamePhase.ROUND_SUMMARY -> {
        RoundSummaryScreen(
          gameState = gameState,
          onPlayNextRound = { viewModel.playNextRound() },
          onReturnToLobby = { viewModel.returnToLobby() },
          modifier = contentModifier
        )
      }
    }
  }
}
