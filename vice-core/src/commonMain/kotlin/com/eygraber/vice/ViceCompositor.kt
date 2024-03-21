package com.eygraber.vice

import androidx.compose.runtime.Composable

public abstract class ViceCompositor<Intent, State> {
  @Composable
  public abstract fun composite(): State

  internal suspend fun internalOnIntent(intent: Intent) = onIntent(intent)
  protected open suspend fun onIntent(intent: Intent) {}

  @Composable
  internal fun internalIsBackHandlerEnabled(): Boolean = isBackHandlerEnabled()

  @Composable
  protected open fun isBackHandlerEnabled(): Boolean = false

  internal fun internalOnBackPressed(emitIntent: (Intent) -> Unit) = onBackPressed(emitIntent)
  protected open fun onBackPressed(emitIntent: (Intent) -> Unit) {}
}
