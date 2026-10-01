package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun RoundSummaryScreen(
  gameState: GameState,
  onPlayNextRound: () -> Unit,
  onReturnToLobby: () -> Unit,
  modifier: Modifier = Modifier
) {
  val imposter = gameState.shuffledOrder.find { it.isImposter }
  val innocentsWon = gameState.innocentsWon
  val word = gameState.currentWord

  val bannerColor = if (innocentsWon) NeonEmerald else ImposterRed

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
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Victory Icon Badge
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(bannerColor.copy(alpha = 0.2f))
            .border(2.dp, bannerColor, CircleShape)
        ) {
          Icon(
            imageVector = if (innocentsWon) Icons.Default.EmojiEvents else Icons.Default.Psychology,
            contentDescription = null,
            tint = bannerColor,
            modifier = Modifier.size(44.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = if (innocentsWon) "DETECTIVES WIN!" else "IMPOSTER WINS!",
          fontSize = 28.sp,
          fontWeight = FontWeight.Black,
          color = bannerColor,
          letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = gameState.roundWinnerMessage,
          fontSize = 14.sp,
          lineHeight = 20.sp,
          color = TextPrimary,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Full Secret Word & Hint Reveal Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DarkSurfaceBorder, RoundedCornerShape(20.dp))
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "ROUND ${gameState.roundNumber} REVEAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = NeonCyan,
                letterSpacing = 1.sp
              )
              Text(
                text = word?.category ?: "General",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "Secret Word:",
              fontSize = 12.sp,
              color = TextSecondary
            )
            Text(
              text = word?.word ?: "Unknown",
              fontSize = 26.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
              Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                tint = NeonAmber,
                modifier = Modifier
                  .size(18.dp)
                  .padding(top = 2.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = "The Imposter's Secret Hint:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = NeonAmber
                )
                val hintVal = gameState.activeHint.ifEmpty { word?.primaryHint ?: "" }
                Text(
                  text = "\"$hintVal\"",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = NeonAmber
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Imposter Identity Row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceVariant)
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (imposter != null) {
                AvatarCircle(
                  name = imposter.name,
                  colorIndex = imposter.avatarColorIndex,
                  size = 32.dp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Secret Imposter: ${imposter?.name ?: "Unknown"}",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = ImposterRed
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Leaderboard / Scoreboard Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "SCOREBOARD",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = NeonPurpleLight,
                letterSpacing = 1.sp
              )
              Text(
                text = "Total Points",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val sortedPlayers = gameState.players.sortedByDescending { it.score }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              sortedPlayers.forEachIndexed { rank, player ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "#${rank + 1}",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = when (rank) {
                        0 -> NeonAmber
                        1 -> NeonCyan
                        else -> TextSecondary
                      },
                      modifier = Modifier.width(26.dp)
                    )
                    AvatarCircle(
                      name = player.name,
                      colorIndex = player.avatarColorIndex,
                      size = 28.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = player.name,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = TextPrimary
                    )
                  }

                  Text(
                    text = "${player.score} pts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonAmber
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Bottom Navigation Buttons
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Button(
          onClick = onPlayNextRound,
          colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .testTag("play_next_round_button")
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "PLAY NEXT ROUND",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            letterSpacing = 1.sp,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onReturnToLobby,
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("return_to_lobby_button")
        ) {
          Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Return to Lobby / Settings", fontSize = 13.sp)
        }
      }
    }
  }
}
