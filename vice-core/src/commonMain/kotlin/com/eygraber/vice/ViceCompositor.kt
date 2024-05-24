package com.eygraber.vice

import androidx.compose.runtime.Composable

public interface ViceCompositor<Intent, State> {
  @Composable
  public fun composite(): State

  public suspend fun onIntent(intent: Intent)
}
