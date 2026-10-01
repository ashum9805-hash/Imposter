package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ImposterRed
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SuspenseScreen(
  gameState: GameState,
  onRevealIdentity: () -> Unit,
  modifier: Modifier = Modifier
) {
  val accused = gameState.shuffledOrder.find { it.id == gameState.accusedPlayerId }
  val pulse = remember { Animatable(1f) }

  LaunchedEffect(Unit) {
    pulse.animateTo(
      targetValue = 1.08f,
      animationSpec = infiniteRepeatable(
        animation = tween(600),
        repeatMode = RepeatMode.Reverse
      )
    )
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
      .padding(24.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "THE MOMENT OF TRUTH",
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        color = NeonAmber,
        letterSpacing = 2.sp
      )

      Spacer(modifier = Modifier.height(28.dp))

      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .scale(pulse.value)
          .size(140.dp)
          .clip(CircleShape)
          .background(DarkSurfaceVariant)
          .border(3.dp, ImposterRed, CircleShape)
      ) {
        if (accused != null) {
          AvatarCircle(
            name = accused.name,
            colorIndex = accused.avatarColorIndex,
            size = 110.dp
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = accused?.name ?: "Accused Player",
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Is ${accused?.name ?: "this player"} the secret Imposter?",
        fontSize = 16.sp,
        color = TextSecondary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(40.dp))

      Button(
        onClick = onRevealIdentity,
        colors = ButtonDefaults.buttonColors(containerColor = ImposterRed),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp)
          .testTag("reveal_identity_button")
      ) {
        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
          text = "REVEAL IDENTITY",
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          letterSpacing = 1.sp,
          color = Color.White
        )
      }
    }
  }
}
