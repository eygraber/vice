package com.eygraber.vice

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

public abstract class ViceCompositor<Intent, State> {
  @Composable
  internal fun internalComposite(intents: Flow<Intent>): State = composite(intents)

  @Composable
  protected abstract fun composite(intents: Flow<Intent>): State

  internal suspend fun internalOnIntent(intent: Intent) = onIntent(intent)
  protected open suspend fun onIntent(intent: Intent) {}

  @Composable
  internal fun internalIsBackHandlerEnabled(): Boolean = isBackHandlerEnabled()

  @Composable
  protected open fun isBackHandlerEnabled(): Boolean = false

  internal fun internalOnBackPressed(emitIntent: (Intent) -> Unit) = onBackPressed(emitIntent)
  protected open fun onBackPressed(emitIntent: (Intent) -> Unit) {}
}
