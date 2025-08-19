package com.eygraber.vice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public interface ViceEffects {
  @Suppress("InjectDispatcher")
  public val dispatcher: CoroutineDispatcher get() = Dispatchers.Default

  public fun CoroutineScope.runEffects()

  public companion object {
    public val None: ViceEffects = object : ViceEffects {
      override fun CoroutineScope.runEffects() {}
    }
  }
}

@Composable
internal fun ViceEffects.Launch() {
  if(this == ViceEffects.None) return

  LaunchedEffect(Unit) {
    launch(dispatcher) {
      runEffects()
    }
  }
}
