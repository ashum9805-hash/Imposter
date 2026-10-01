package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ImposterRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight

@Composable
fun HowToPlayDialog(
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("how_to_play_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "HOW TO PLAY",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = NeonPurpleLight,
            letterSpacing = 1.sp
          )
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_rules_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RuleStepItem(
          icon = Icons.Default.Casino,
          iconColor = NeonCyan,
          stepNumber = "1",
          title = "Real-Time Fair Shuffling",
          description = "The app cryptographically shuffles both the pass-and-play order and the secret Imposter role. Zero position bias—it never just picks whoever is in the middle!"
        )

        Spacer(modifier = Modifier.height(12.dp))

        RuleStepItem(
          icon = Icons.Default.Search,
          iconColor = NeonEmerald,
          stepNumber = "2",
          title = "Secret Pass & Peek",
          description = "Pass the phone around. Innocents see the secret word (e.g., 'Pizza'). The Imposter does NOT see the word—only a subtle clue (e.g., 'Baked dough with savory toppings')."
        )

        Spacer(modifier = Modifier.height(12.dp))

        RuleStepItem(
          icon = Icons.Default.RecordVoiceOver,
          iconColor = NeonPurpleLight,
          stepNumber = "3",
          title = "Define Without Saying",
          description = "In clockwise or shuffled order, each player gives ONE clue or definition about the secret word. Innocents prove they know the word; the Imposter bluffs to blend in!"
        )

        Spacer(modifier = Modifier.height(12.dp))

        RuleStepItem(
          icon = Icons.Default.Psychology,
          iconColor = ImposterRed,
          stepNumber = "4",
          title = "Accuse & Last Stand",
          description = "Vote on who is the Imposter. If caught, the Imposter gets one last stand: guess the exact secret word to steal victory!"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("got_it_rules_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text("GOT IT, LET'S PLAY!", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun RuleStepItem(
  icon: ImageVector,
  iconColor: Color,
  stepNumber: String,
  title: String,
  description: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(DarkSurfaceVariant)
      .padding(12.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(iconColor.copy(alpha = 0.2f))
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconColor,
        modifier = Modifier.size(20.dp)
      )
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = 12.dp)
    ) {
      Text(
        text = "$stepNumber. $title",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = description,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        color = Color.White.copy(alpha = 0.8f)
      )
    }
  }
}
