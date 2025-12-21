package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

public abstract class StateFlowSource<T> : ViceSource<T> {
  protected abstract val flow: StateFlow<T>

  public val value: T get() = flow.value

  protected abstract suspend fun onAttached(scope: CoroutineScope)

  @Composable
  final override fun currentState(): T {
    LaunchedEffect(Unit) {
      onAttached(this)
    }

    return flow.collectAsStateWithLifecycle().value
  }
}
