package com.example.model

import java.util.UUID

data class Player(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val avatarColorIndex: Int = 0,
  val isImposter: Boolean = false,
  val roleSeen: Boolean = false,
  val votesReceived: Int = 0,
  val score: Int = 0
)
