package com.eygraber.vice.samples.shared.home

import com.eygraber.vice.samples.shared.TodoItem

sealed interface HomeIntent {
  data object AddItem : HomeIntent

  data class ToggleItemCompletion(val item: TodoItem) : HomeIntent

  data class NavigateToDetails(val id: String) : HomeIntent
  data object NavigateToSettings : HomeIntent
}
