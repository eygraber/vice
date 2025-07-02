package com.eygraber.vice.samples.shared

data class TodoItem(
  val id: String,
  val completed: Boolean,
  val title: String,
  val description: String = "",
)
