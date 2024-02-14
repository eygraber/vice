package com.eygraber.vice

import androidx.compose.runtime.Composable

public interface ViceView<Intent, State> {
  @Composable
  public fun Render(state: State, onIntent: (Intent) -> Unit)
}
