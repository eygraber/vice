package com.eygraber.vice.samples.shared.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.samples.shared.TodoRepo

class HomeCompositor(
  private val onNavigateToCreateItem: () -> Unit,
  private val onNavigateToUpdateItem: (String) -> Unit,
  private val onNavigateToSettings: () -> Unit,
) : ViceCompositor<HomeIntent, HomeViewState> {
  @Composable
  override fun composite() = HomeViewState(
    items = TodoRepo.items.collectAsState().value,
  )

  override suspend fun onIntent(intent: HomeIntent) {
    when(intent) {
      HomeIntent.AddItem -> onNavigateToCreateItem()
      is HomeIntent.ToggleItemCompletion -> TodoRepo.updateItem(
        newItem = intent.item.copy(
          isCompleted = !intent.item.isCompleted,
        ),
      )
      is HomeIntent.NavigateToDetails -> onNavigateToUpdateItem(intent.id)
      HomeIntent.NavigateToSettings -> onNavigateToSettings()
    }
  }
}
