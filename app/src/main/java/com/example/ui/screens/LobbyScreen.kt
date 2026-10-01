package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.WordRepository
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
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LobbyScreen(
  gameState: GameState,
  onAddPlayer: (String) -> Unit,
  onRemovePlayer: (String) -> Unit,
  onUpdatePlayerName: (String, String) -> Unit,
  onClearAllPlayers: () -> Unit,
  onSetQuickPlayers: (Int) -> Unit,
  onToggleCategory: (String) -> Unit,
  onSetTimerDuration: (Int) -> Unit,
  onSetImposterCount: (Int) -> Unit,
  onStartGame: () -> Unit,
  onBackToLanding: () -> Unit,
  onAddCustomWord: (String, String, String, List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  var newPlayerName by remember { mutableStateOf("") }
  var showRulesDialog by remember { mutableStateOf(false) }
  var showCustomWordDialog by remember { mutableStateOf(false) }
  var editingPlayer by remember { mutableStateOf<Player?>(null) }

  if (showRulesDialog) {
    HowToPlayDialog(onDismiss = { showRulesDialog = false })
  }

  if (showCustomWordDialog) {
    CustomWordDialog(
      onDismiss = { showCustomWordDialog = false },
      onAddWord = onAddCustomWord
    )
  }

  // Edit Player Dialog
  if (editingPlayer != null) {
    var editedName by remember(editingPlayer) { mutableStateOf(editingPlayer!!.name) }
    Dialog(onDismissRequest = { editingPlayer = null }) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .testTag("edit_player_dialog")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "EDIT PLAYER NAME",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = NeonPurpleLight
            )
            IconButton(onClick = { editingPlayer = null }) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = editedName,
            onValueChange = { editedName = it },
            label = { Text("Player Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NeonPurple,
              unfocusedBorderColor = DarkSurfaceBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_player_name_input")
          )

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            OutlinedButton(
              onClick = { editingPlayer = null },
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Cancel", color = TextSecondary)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
              onClick = {
                if (editedName.isNotBlank()) {
                  onUpdatePlayerName(editingPlayer!!.id, editedName)
                  editingPlayer = null
                }
              },
              enabled = editedName.isNotBlank(),
              colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("save_player_name_button")
            ) {
              Text("Save Name", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Top Bar: Back & Rules
      item {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { onBackToLanding() }
              .padding(4.dp)
          ) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to home",
              tint = TextPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "HOME",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextSecondary,
              letterSpacing = 1.sp
            )
          }

          IconButton(
            onClick = { showRulesDialog = true },
            modifier = Modifier.testTag("rules_button")
          ) {
            Icon(
              Icons.Default.HelpOutline,
              contentDescription = "Rules",
              tint = NeonCyan
            )
          }
        }
      }

      // Player Management Section (Up to 20 players, with editing)
      item {
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
              Column {
                Text(
                  text = "PLAYERS (${gameState.players.size}/20)",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = TextPrimary
                )
                Text(
                  text = "Tap any name or pencil to rename",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
              }

              if (gameState.players.size < 3) {
                Text(
                  text = "Min 3 to play",
                  fontSize = 12.sp,
                  color = ImposterRed,
                  fontWeight = FontWeight.SemiBold
                )
              } else {
                Text(
                  text = "✓ Ready",
                  fontSize = 12.sp,
                  color = NeonEmerald,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player Input
            if (gameState.players.size < 20) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = newPlayerName,
                  onValueChange = { newPlayerName = it },
                  placeholder = { Text("Enter player name...", color = TextSecondary) },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                  ),
                  keyboardActions = KeyboardActions(
                    onDone = {
                      if (newPlayerName.isNotBlank()) {
                        onAddPlayer(newPlayerName)
                        newPlayerName = ""
                      }
                    }
                  ),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("player_name_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                  onClick = {
                    if (newPlayerName.isNotBlank()) {
                      onAddPlayer(newPlayerName)
                      newPlayerName = ""
                    }
                  },
                  enabled = newPlayerName.isNotBlank(),
                  colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .height(56.dp)
                    .testTag("add_player_button")
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Add player")
                }
              }
            } else {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(DarkSurfaceVariant)
                  .padding(12.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "Maximum group size (20 players) reached!",
                  color = NeonAmber,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Presets & Clear All Button
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Quick fill:", fontSize = 11.sp, color = TextSecondary)
                listOf(4, 6, 8, 12).forEach { count ->
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(DarkSurfaceVariant)
                      .clickable { onSetQuickPlayers(count) }
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text("$count", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                  }
                }
              }

              if (gameState.players.isNotEmpty()) {
                Text(
                  text = "Clear All",
                  fontSize = 12.sp,
                  color = ImposterRed,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier
                    .clickable { onClearAllPlayers() }
                    .padding(4.dp)
                    .testTag("clear_all_players_button")
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player List Rows with inline Rename / Edit Pencil
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              gameState.players.forEachIndexed { index, player ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                      .weight(1f)
                      .clickable { editingPlayer = player }
                  ) {
                    Text(
                      text = "${index + 1}.",
                      fontSize = 12.sp,
                      color = TextSecondary,
                      modifier = Modifier.width(24.dp)
                    )
                    AvatarCircle(
                      name = player.name,
                      colorIndex = player.avatarColorIndex,
                      size = 32.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = player.name,
                      fontWeight = FontWeight.SemiBold,
                      color = TextPrimary,
                      fontSize = 14.sp
                    )
                    if (player.score > 0) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "(${player.score} pts)",
                        fontSize = 11.sp,
                        color = NeonAmber
                      )
                    }
                  }

                  Row(verticalAlignment = Alignment.CenterVertically) {
                    // Edit Name Pencil
                    IconButton(
                      onClick = { editingPlayer = player },
                      modifier = Modifier
                        .size(28.dp)
                        .testTag("edit_player_${player.name}")
                    ) {
                      Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit ${player.name}",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                      )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Remove Player
                    IconButton(
                      onClick = { onRemovePlayer(player.id) },
                      modifier = Modifier.size(28.dp)
                    ) {
                      Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove ${player.name}",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Game Rules & Settings
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "ROUND SETTINGS",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imposter Count Selection
            val maxImposters = ((gameState.players.size - 1) / 2).coerceIn(1, 4)
            Text(
              text = "Secret Imposters: ${gameState.imposterCount}",
              fontSize = 13.sp,
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              (1..maxImposters.coerceAtLeast(1)).forEach { count ->
                val selected = gameState.imposterCount == count
                FilterChip(
                  selected = selected,
                  onClick = { onSetImposterCount(count) },
                  label = {
                    Text(
                      if (count == 1) "1 Imposter" else "$count Imposters",
                      fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ImposterRed,
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextPrimary
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timer Selection
            Text(
              text = "Discussion Timer",
              fontSize = 13.sp,
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              listOf(60 to "60s", 90 to "90s", 120 to "2m").forEach { (sec, label) ->
                val selected = gameState.timerDurationSeconds == sec
                FilterChip(
                  selected = selected,
                  onClick = { onSetTimerDuration(sec) },
                  label = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonCyan,
                    selectedLabelColor = Color.Black,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextPrimary
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Categories
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Categories (${if (gameState.selectedCategories.isEmpty()) "All" else gameState.selectedCategories.size})",
                fontSize = 13.sp,
                color = TextSecondary
              )
              Text(
                text = "+ Custom Word",
                fontSize = 12.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                  .clickable { showCustomWordDialog = true }
                  .testTag("add_custom_word_chip")
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              WordRepository.CATEGORIES.forEach { category ->
                val isSelected = gameState.selectedCategories.contains(category)
                FilterChip(
                  selected = isSelected,
                  onClick = { onToggleCategory(category) },
                  label = { Text(category, fontSize = 12.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonPurple,
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                  )
                )
              }
            }
          }
        }
      }

      // Start Game CTA Button
      item {
        val canStart = gameState.players.size >= 3
        Button(
          onClick = onStartGame,
          enabled = canStart,
          colors = ButtonDefaults.buttonColors(
            containerColor = NeonPurpleLight,
            disabledContainerColor = DarkSurfaceVariant
          ),
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .testTag("start_game_button")
        ) {
          Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = if (canStart) Color.Black else TextSecondary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (canStart) "START GAME" else "ADD AT LEAST 3 PLAYERS",
            color = if (canStart) Color.Black else TextSecondary,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            letterSpacing = 1.sp
          )
        }
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
