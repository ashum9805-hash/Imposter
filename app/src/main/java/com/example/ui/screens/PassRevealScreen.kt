package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.PrivacyHoldButton
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.DetectiveBlue
import com.example.ui.theme.ImposterRed
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PassRevealScreen(
  gameState: GameState,
  onHoldChange: (Boolean) -> Unit,
  onNextPlayer: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentPlayer = gameState.shuffledOrder.getOrNull(gameState.currentPassIndex)
  val isLastPlayer = gameState.currentPassIndex >= gameState.shuffledOrder.size - 1
  val word = gameState.currentWord

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
      // Top Status / Turn Indicator
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
            text = "PASS & REVEAL",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            letterSpacing = 1.sp
          )
          Text(
            text = "Player ${gameState.currentPassIndex + 1} of ${gameState.shuffledOrder.size}",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
          progress = { (gameState.currentPassIndex + 1) / gameState.shuffledOrder.size.toFloat() },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = NeonCyan,
          trackColor = DarkSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Target Player Name Callout
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (currentPlayer != null) {
              AvatarCircle(
                name = currentPlayer.name,
                colorIndex = currentPlayer.avatarColorIndex,
                size = 56.dp
              )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Text(
                text = "PASS PHONE TO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
              )
              Text(
                text = currentPlayer?.name ?: "Next Player",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Secret Card Area (Hidden vs Revealed)
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      ) {
        if (!gameState.isSecretVisible) {
          // Privacy Shielded State
          Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            modifier = Modifier
              .fillMaxWidth()
              .border(1.5.dp, DarkSurfaceBorder, RoundedCornerShape(24.dp))
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(80.dp)
                  .clip(CircleShape)
                  .background(DarkSurface)
                  .border(2.dp, NeonPurple.copy(alpha = 0.5f), CircleShape)
              ) {
                Icon(
                  Icons.Default.Lock,
                  contentDescription = "Hidden",
                  tint = NeonPurpleLight,
                  modifier = Modifier.size(40.dp)
                )
              }

              Spacer(modifier = Modifier.height(20.dp))

              Text(
                text = "KEEP SCREEN PRIVATE",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = TextPrimary,
                letterSpacing = 1.sp
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "Make sure only ${currentPlayer?.name ?: "the player"} can see the screen. Hold down the button below to secretly peek your role!",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextSecondary
              )
            }
          }
        } else {
          // Revealed Secret State!
          val isImposter = currentPlayer?.isImposter == true

          Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isImposter) DarkSurface else DarkSurface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                  if (isImposter) listOf(ImposterRed, NeonPurple)
                  else listOf(NeonEmerald, NeonCyan)
                ),
                shape = RoundedCornerShape(24.dp)
              )
              .testTag("secret_role_card")
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
            ) {
              // Role Badge
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    if (isImposter) ImposterRed.copy(alpha = 0.2f)
                    else NeonEmerald.copy(alpha = 0.2f)
                  )
                  .border(
                    width = 1.dp,
                    color = if (isImposter) ImposterRed else NeonEmerald,
                    shape = RoundedCornerShape(12.dp)
                  )
                  .padding(horizontal = 14.dp, vertical = 6.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (isImposter) Icons.Default.Psychology else Icons.Default.Search,
                    contentDescription = null,
                    tint = if (isImposter) ImposterRed else NeonEmerald,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isImposter) "YOU ARE THE IMPOSTER" else "INNOCENT DETECTIVE",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp,
                    color = if (isImposter) ImposterRed else NeonEmerald
                  )
                }
              }

              Spacer(modifier = Modifier.height(18.dp))

              Text(
                text = "CATEGORY: ${word?.category?.uppercase() ?: "GENERAL"}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 1.sp
              )

              Spacer(modifier = Modifier.height(10.dp))

              if (!isImposter) {
                // Innocent Player Secret Word
                Text(
                  text = word?.word ?: "UNKNOWN",
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 2.sp,
                  color = Color.White,
                  textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "When discussion starts, define or describe this word with subtle clues so other innocents trust you, but don't give the word away to the imposter!",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                  )
                }
              } else {
                // Imposter gets the Hint only!
                Text(
                  text = "HIDDEN ???",
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Black,
                  color = ImposterRed,
                  letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = ImposterRed.copy(alpha = 0.15f)),
                  modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ImposterRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                  Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(20.dp)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "YOUR SECRET HINT",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = NeonAmber,
                        letterSpacing = 1.sp
                      )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val hintText = gameState.activeHint.ifEmpty { word?.primaryHint ?: "" }
                    Text(
                      text = "\"$hintText\"",
                      fontSize = 24.sp,
                      fontWeight = FontWeight.Black,
                      color = NeonAmber,
                      textAlign = TextAlign.Center
                    )
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                  text = "Use this short clue to bluff! You don't know the exact word, so listen closely to what others say.",
                  fontSize = 12.sp,
                  lineHeight = 16.sp,
                  color = TextSecondary,
                  textAlign = TextAlign.Center
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom Control Area: Hold Button & Pass Button
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrivacyHoldButton(
          isHolding = gameState.isSecretVisible,
          onHoldChange = onHoldChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onNextPlayer,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isLastPlayer) NeonEmerald else NeonPurple
          ),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("next_player_pass_button")
        ) {
          Text(
            text = if (isLastPlayer) "START DISCUSSION & TIMER" else "NEXT PLAYER PASS",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (isLastPlayer) Color.Black else Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = if (isLastPlayer) Color.Black else Color.White
          )
        }
      }
    }
  }
}
