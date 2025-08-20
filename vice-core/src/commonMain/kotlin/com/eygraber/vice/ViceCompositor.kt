package com.eygraber.vice

import androidx.compose.runtime.Composable

/**
 * It is inadvisable to override [equals] and [hashCode] for a [ViceCompositor].
 */
public interface ViceCompositor<Intent, State> {
  @Composable
  public fun composite(): State

  public suspend fun onIntent(intent: Intent)
}
