package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCoral
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight

val AvatarGradients = listOf(
  listOf(NeonPurple, NeonPurpleLight),
  listOf(NeonCyan, Color(0xFF0077B6)),
  listOf(NeonEmerald, Color(0xFF00B4D8)),
  listOf(NeonCoral, NeonAmber),
  listOf(Color(0xFFFF007F), NeonPurple),
  listOf(NeonAmber, Color(0xFFFF5400)),
  listOf(Color(0xFF7209B7), Color(0xFFF72585)),
  listOf(Color(0xFF4CC9F0), Color(0xFF4361EE))
)

@Composable
fun AvatarCircle(
  name: String,
  colorIndex: Int = 0,
  size: Dp = 48.dp,
  modifier: Modifier = Modifier
) {
  val gradientColors = AvatarGradients[colorIndex % AvatarGradients.size]
  val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(Brush.linearGradient(gradientColors))
      .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
  ) {
    Text(
      text = initial,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = (size.value * 0.45f).sp
    )
  }
}
