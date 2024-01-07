package com.eygraber.vice

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

public abstract class ViceCompositor<Intent, State> {
  @Composable
  internal abstract fun composite(intents: Flow<Intent>): State

  internal open suspend fun onIntent(intent: Intent) {}

  @Composable
  internal open fun isBackHandlerEnabled(): Boolean = false

  internal open fun onBackPressed(emitIntent: (Intent) -> Unit) {}
}
