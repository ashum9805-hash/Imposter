package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.Player
import com.example.ui.components.AvatarCircle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ImposterRed
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun VotingScreen(
  gameState: GameState,
  onCastVote: (String) -> Unit,
  onResetVotes: () -> Unit,
  onAccusePlayer: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  // Find player with highest votes or local selected target
  val highestVotedPlayer = gameState.shuffledOrder.maxByOrNull { it.votesReceived }
  var selectedPlayerId by remember {
    mutableStateOf(highestVotedPlayer?.id ?: gameState.shuffledOrder.firstOrNull()?.id)
  }

  val accusedPlayer = gameState.shuffledOrder.find { it.id == selectedPlayerId }

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
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ACCUSATION & VOTING",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ImposterRed,
            letterSpacing = 1.sp
          )
          OutlinedButton(
            onClick = onResetVotes,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("reset_votes_button")
          ) {
            Icon(
              Icons.Default.RestartAlt,
              contentDescription = "Reset votes",
              tint = TextSecondary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reset", fontSize = 11.sp, color = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "WHO IS THE IMPOSTER?",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = TextPrimary,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Tap a player to select them as the primary suspect, or tap the vote button to tally hand raises.",
          fontSize = 13.sp,
          color = TextSecondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Player Voting Cards
        Column(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          gameState.shuffledOrder.forEach { player ->
            val isSelected = player.id == selectedPlayerId
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) DarkSurfaceVariant else DarkSurface
              ),
              modifier = Modifier
                .fillMaxWidth()
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) ImposterRed else DarkSurfaceBorder,
                  shape = RoundedCornerShape(16.dp)
                )
                .clickable { selectedPlayerId = player.id }
                .testTag("vote_player_card_${player.name}")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  AvatarCircle(
                    name = player.name,
                    colorIndex = player.avatarColorIndex,
                    size = 44.dp
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = player.name,
                      fontSize = 17.sp,
                      fontWeight = FontWeight.Bold,
                      color = TextPrimary
                    )
                    if (isSelected) {
                      Text(
                        text = "PRIME SUSPECT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = ImposterRed,
                        letterSpacing = 0.5.sp
                      )
                    }
                  }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  // Vote Count Chip
                  if (player.votesReceived > 0) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ImposterRed.copy(alpha = 0.2f))
                        .border(1.dp, ImposterRed, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text(
                        text = "${player.votesReceived} ${if (player.votesReceived == 1) "vote" else "votes"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImposterRed
                      )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                  }

                  // +1 Vote Button
                  Button(
                    onClick = {
                      onCastVote(player.id)
                      selectedPlayerId = player.id
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("cast_vote_${player.name}")
                  ) {
                    Icon(
                      Icons.Default.HowToVote,
                      contentDescription = "Vote for ${player.name}",
                      tint = NeonCyan,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+1", color = NeonCyan, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Bottom Accuse CTA
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Button(
          onClick = {
            if (selectedPlayerId != null) {
              onAccusePlayer(selectedPlayerId!!)
            }
          },
          enabled = selectedPlayerId != null,
          colors = ButtonDefaults.buttonColors(containerColor = ImposterRed),
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .testTag("accuse_player_button")
        ) {
          Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ACCUSE ${accusedPlayer?.name?.uppercase() ?: "PLAYER"}",
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            letterSpacing = 1.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
