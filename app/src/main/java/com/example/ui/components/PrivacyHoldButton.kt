package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan

@Composable
fun PrivacyHoldButton(
  isHolding: Boolean,
  onHoldChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val pulseScale = remember { Animatable(1f) }

  LaunchedEffect(isHolding) {
    if (!isHolding) {
      pulseScale.animateTo(
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
          animation = tween(800),
          repeatMode = RepeatMode.Reverse
        )
      )
    } else {
      pulseScale.snapTo(0.97f)
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
      .scale(pulseScale.value)
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            onHoldChange(true)
            try {
              awaitRelease()
            } finally {
              onHoldChange(false)
            }
          }
        )
      }
  ) {
    Button(
      onClick = {
        // Fallback toggle for non-touch accessibility
        onHoldChange(!isHolding)
      },
      colors = ButtonDefaults.buttonColors(
        containerColor = if (isHolding) NeonAmber else NeonCyan,
        contentColor = Color.Black
      ),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .testTag("privacy_hold_button")
    ) {
      Icon(
        imageVector = if (isHolding) Icons.Default.Visibility else Icons.Default.VisibilityOff,
        contentDescription = if (isHolding) "Revealing role" else "Hidden role",
        modifier = Modifier.padding(end = 8.dp)
      )
      Text(
        text = if (isHolding) "RELEASING WILL HIDE ROLE" else "HOLD DOWN TO PEEK SECRET ROLE",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.5.sp
      )
    }
  }
}
