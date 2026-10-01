package com.example.model

data class WordItem(
  val id: String,
  val word: String,
  val category: String,
  val hints: List<String>,
  val distractors: List<String> = emptyList()
) {
  val primaryHint: String get() = hints.firstOrNull() ?: ""
}
