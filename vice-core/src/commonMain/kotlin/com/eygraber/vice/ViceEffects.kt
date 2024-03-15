package com.eygraber.vice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public abstract class ViceEffects {
  internal fun initialize(scope: CoroutineScope) {
    scope.onInitialized()
  }

  protected abstract fun CoroutineScope.onInitialized()
}

@Composable
internal fun ViceEffects.Launch() {
  LaunchedEffect(Unit) {
    @Suppress("InjectDispatcher")
    launch(Dispatchers.Default) {
      initialize(this)
    }
  }
}
