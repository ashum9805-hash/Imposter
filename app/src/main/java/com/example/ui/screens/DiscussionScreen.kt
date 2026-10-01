package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.components.AvatarCircle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ImposterRed
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DiscussionScreen(
  gameState: GameState,
  onPauseResumeTimer: () -> Unit,
  onAdd30Seconds: () -> Unit,
  onNextSpeaker: () -> Unit,
  onProceedToVoting: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentSpeaker = gameState.shuffledOrder.getOrNull(gameState.activeSpeakerIndex)
  val minutes = gameState.discussionSecondsRemaining / 60
  val seconds = gameState.discussionSecondsRemaining % 60
  val timeFormatted = "%02d:%02d".format(minutes, seconds)

  val timerProgress = if (gameState.timerDurationSeconds > 0) {
    (gameState.discussionSecondsRemaining.toFloat() / gameState.timerDurationSeconds.toFloat()).coerceIn(0f, 1f)
  } else 1f

  val timerColor = when {
    gameState.discussionSecondsRemaining <= 15 -> ImposterRed
    gameState.discussionSecondsRemaining <= 30 -> NeonAmber
    else -> NeonCyan
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Header Section
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "DISCUSSION PHASE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = NeonPurpleLight,
            letterSpacing = 1.sp
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(NeonCyan.copy(alpha = 0.15f))
              .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = gameState.currentWord?.category ?: "General",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = NeonCyan
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Circular Timer
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.size(190.dp)
        ) {
          CircularProgressIndicator(
            progress = { timerProgress },
            modifier = Modifier.size(180.dp),
            color = timerColor,
            strokeWidth = 10.dp,
            trackColor = DarkSurfaceVariant
          )

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = timeFormatted,
              fontSize = 42.sp,
              fontWeight = FontWeight.Black,
              color = timerColor
            )
            Text(
              text = if (gameState.isTimerRunning) "TIME REMAINING" else "PAUSED",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TextSecondary,
              letterSpacing = 1.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Timer Controls
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = onPauseResumeTimer,
            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("toggle_timer_button")
          ) {
            Icon(
              imageVector = if (gameState.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (gameState.isTimerRunning) "Pause" else "Resume",
              tint = NeonCyan
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (gameState.isTimerRunning) "Pause" else "Resume",
              color = TextPrimary,
              fontSize = 13.sp
            )
          }

          Button(
            onClick = onAdd30Seconds,
            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("add_time_button")
          ) {
            Icon(Icons.Default.Add, contentDescription = "+30s", tint = NeonEmerald)
            Spacer(modifier = Modifier.width(4.dp))
            Text("+30s", color = TextPrimary, fontSize = 13.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Current Speaker Callout
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(NeonPurple.copy(alpha = 0.2f))
              ) {
                Icon(
                  Icons.Default.Mic,
                  contentDescription = null,
                  tint = NeonPurpleLight,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "CURRENT SPEAKER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPurpleLight,
                letterSpacing = 1.sp
              )
            }

            IconButton(
              onClick = onNextSpeaker,
              modifier = Modifier.size(32.dp).testTag("next_speaker_button")
            ) {
              Icon(
                Icons.Default.SkipNext,
                contentDescription = "Next speaker",
                tint = NeonCyan
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            if (currentSpeaker != null) {
              AvatarCircle(
                name = currentSpeaker.name,
                colorIndex = currentSpeaker.avatarColorIndex,
                size = 48.dp
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = currentSpeaker?.name ?: "Speaker",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
              )
              Text(
                text = "Give 1 clue or definition without saying the secret word!",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Speaker Sequence Order
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "SPEAKER ROTATION",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = TextSecondary,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          itemsIndexed(gameState.shuffledOrder) { index, player ->
            val isCurrent = index == gameState.activeSpeakerIndex
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isCurrent) NeonPurple.copy(alpha = 0.25f) else DarkSurfaceVariant
              ),
              modifier = Modifier
                .border(
                  width = if (isCurrent) 1.5.dp else 1.dp,
                  color = if (isCurrent) NeonPurpleLight else DarkSurfaceBorder,
                  shape = RoundedCornerShape(12.dp)
                )
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AvatarCircle(
                  name = player.name,
                  colorIndex = player.avatarColorIndex,
                  size = 24.dp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = player.name,
                  fontSize = 12.sp,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                  color = if (isCurrent) Color.White else TextSecondary
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Proceed to Vote CTA
      Button(
        onClick = onProceedToVoting,
        colors = ButtonDefaults.buttonColors(containerColor = ImposterRed),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("proceed_to_vote_button")
      ) {
        Icon(Icons.Default.HowToVote, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "TIME TO ACCUSE & VOTE",
          fontWeight = FontWeight.Black,
          fontSize = 15.sp,
          color = Color.White,
          letterSpacing = 1.sp
        )
      }
    }
  }
}
