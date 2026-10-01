package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.WordRepository
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@Composable
fun CustomWordDialog(
  onDismiss: () -> Unit,
  onAddWord: (word: String, category: String, hint: String, distractors: List<String>) -> Unit
) {
  var word by remember { mutableStateOf("") }
  var category by remember { mutableStateOf(WordRepository.CATEGORIES.first()) }
  var hint by remember { mutableStateOf("") }
  var distractorsText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("custom_word_dialog")
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
            text = "ADD CUSTOM WORD",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NeonCyan
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = word,
          onValueChange = { word = it },
          label = { Text("Secret Word (e.g., Lasagna)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_word_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category (e.g., Food & Drink)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_category_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = hint,
          onValueChange = { hint = it },
          label = { Text("Imposter's Hint (e.g., Baked pasta layers, cheese, meat)") },
          minLines = 2,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_hint_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = distractorsText,
          onValueChange = { distractorsText = it },
          label = { Text("Distractor Options (comma separated)") },
          placeholder = { Text("Ravioli, Spaghetti, Ziti", color = Color.Gray) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_distractors_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (word.isNotBlank() && hint.isNotBlank()) {
              val distractors = distractorsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
              onAddWord(word, category, hint, distractors)
              onDismiss()
            }
          },
          enabled = word.isNotBlank() && hint.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("save_custom_word_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
        ) {
          Text("SAVE WORD", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
