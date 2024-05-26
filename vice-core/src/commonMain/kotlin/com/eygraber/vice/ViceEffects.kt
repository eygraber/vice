package com.eygraber.vice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public interface ViceEffects {
  public fun CoroutineScope.runEffects()

  public companion object {
    public val None: ViceEffects = object : ViceEffects {
      override fun CoroutineScope.runEffects() {}
    }
  }
}

@Composable
internal fun ViceEffects.Launch() {
  LaunchedEffect(Unit) {
    @Suppress("InjectDispatcher")
    launch(Dispatchers.Default) {
      runEffects()
    }
  }
}
