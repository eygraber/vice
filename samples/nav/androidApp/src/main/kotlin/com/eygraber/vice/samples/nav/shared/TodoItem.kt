package com.eygraber.vice.samples.nav.shared

data class TodoItem(
  val id: String,
  val completed: Boolean,
  val title: String,
  val description: String = "",
)
