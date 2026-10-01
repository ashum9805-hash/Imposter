package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.GameState
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
fun LandingScreen(
  gameState: GameState,
  onStartPlaying: () -> Unit,
  onAddCustomWord: (String, String, String, List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  var showRulesDialog by remember { mutableStateOf(false) }
  var showCustomWordDialog by remember { mutableStateOf(false) }

  if (showRulesDialog) {
    HowToPlayDialog(onDismiss = { showRulesDialog = false })
  }

  if (showCustomWordDialog) {
    CustomWordDialog(
      onDismiss = { showCustomWordDialog = false },
      onAddWord = onAddCustomWord
    )
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Hero Header Graphic with dynamic gradient
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.imposter_hero_banner_1790856920673),
          contentDescription = "Imposter Party Artwork",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  DarkBackground.copy(alpha = 0.5f),
                  DarkBackground
                )
              )
            )
        )

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "IMPOSTER",
              fontSize = 38.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 3.sp,
              color = NeonPurpleLight
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ImposterRed)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "PARTY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "The Group Word Bluffing Game",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeonCyan,
            letterSpacing = 0.5.sp
          )
        }
      }

      // Action Section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(
          text = "One secret imposter receives only a short hint. Everyone else defines the secret word. Can you spot who's bluffing?",
          fontSize = 13.sp,
          lineHeight = 19.sp,
          color = TextSecondary,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Big Primary CTA: Start Playing
        Button(
          onClick = onStartPlaying,
          colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleLight),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .testTag("start_playing_button")
        ) {
          Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "START PLAYING",
            color = Color.Black,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            letterSpacing = 1.sp
          )
        }

        // Secondary Button: How to Play
        OutlinedButton(
          onClick = { showRulesDialog = true },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("how_to_play_button")
        ) {
          Icon(
            Icons.Default.HelpOutline,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "HOW TO PLAY & RULES",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        // Tertiary Button: Add Custom Words
        OutlinedButton(
          onClick = { showCustomWordDialog = true },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("custom_word_pack_button")
        ) {
          Icon(
            Icons.Default.Lightbulb,
            contentDescription = null,
            tint = NeonAmber,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "CREATE CUSTOM WORD & HINT",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Feature Highlights
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
              .weight(1f)
              .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(NeonCyan.copy(alpha = 0.15f))
              ) {
                Icon(Icons.Default.Group, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text("3 to 20 Players", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
              Text("Pass & play on 1 phone", fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.Center)
            }
          }

          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
              .weight(1f)
              .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(NeonEmerald.copy(alpha = 0.15f))
              ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text("Offline Party", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
              Text("No Wi-Fi needed", fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.Center)
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
