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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import java.security.SecureRandom

@Composable
fun ImposterGuessScreen(
  gameState: GameState,
  onSubmitGuess: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val imposter = gameState.shuffledOrder.find { it.isImposter }
  val secretWord = gameState.currentWord?.word ?: ""
  val distractors = gameState.currentWord?.distractors ?: emptyList()

  // Generate scrambled choices including the real word
  val options = remember(secretWord, distractors) {
    (listOf(secretWord) + distractors).shuffled(SecureRandom())
  }

  var selectedOption by remember { mutableStateOf<String?>(null) }
  var customGuessText by remember { mutableStateOf("") }

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
        // Banner: Imposter Caught!
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ImposterRed.copy(alpha = 0.2f))
            .border(1.dp, ImposterRed, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = ImposterRed, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "IMPOSTER BUSTED!",
              fontWeight = FontWeight.Black,
              fontSize = 13.sp,
              color = ImposterRed,
              letterSpacing = 1.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "IMPOSTER'S LAST STAND",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = NeonAmber,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "${imposter?.name ?: "The Imposter"} was caught! However, if they can guess the secret word right now, they steal the victory from the detectives!",
          fontSize = 13.sp,
          lineHeight = 18.sp,
          color = TextSecondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Clue hint review card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = NeonAmber)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "CATEGORY: ${gameState.currentWord?.category?.uppercase() ?: "GENERAL"}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
              )
              val hintVal = gameState.activeHint.ifEmpty { gameState.currentWord?.primaryHint ?: "" }
              Text(
                text = "Hint: \"$hintVal\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonAmber
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "SELECT OR GUESS THE WORD",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = TextSecondary,
          letterSpacing = 1.sp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, bottom = 8.dp)
        )

        // Multiple choice options
        Column(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          options.forEach { option ->
            val isSelected = selectedOption == option && customGuessText.isBlank()
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) NeonPurple.copy(alpha = 0.25f) else DarkSurfaceVariant
              ),
              modifier = Modifier
                .fillMaxWidth()
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) NeonPurpleLight else DarkSurfaceBorder,
                  shape = RoundedCornerShape(14.dp)
                )
                .clickable {
                  selectedOption = option
                  customGuessText = ""
                }
                .testTag("guess_option_$option")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = option,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else TextPrimary
                )

                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = "Selected", tint = NeonPurpleLight)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Or custom typed guess
        OutlinedTextField(
          value = customGuessText,
          onValueChange = {
            customGuessText = it
            if (it.isNotBlank()) selectedOption = null
          },
          label = { Text("Or type a custom word guess...") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = DarkSurfaceBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_word_guess_input")
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Submit Guess Button
      val finalGuess = customGuessText.trim().ifEmpty { selectedOption ?: "" }
      Button(
        onClick = {
          if (finalGuess.isNotBlank()) {
            onSubmitGuess(finalGuess)
          }
        },
        enabled = finalGuess.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = NeonAmber),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(58.dp)
          .testTag("submit_imposter_guess_button")
      ) {
        Text(
          text = "SUBMIT FINAL GUESS",
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          letterSpacing = 1.sp,
          color = Color.Black
        )
      }
    }
  }
}
