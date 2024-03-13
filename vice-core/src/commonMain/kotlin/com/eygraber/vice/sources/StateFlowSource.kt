package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

public abstract class StateFlowSource<T> : ViceSource<T>, State<T> {
  protected abstract val flow: StateFlow<T>

  protected abstract suspend fun onAttached(scope: CoroutineScope)

  override val value: T get() = flow.value

  @Composable
  final override fun currentState(): T {
    LaunchedEffect(Unit) {
      onAttached(this)
    }

    return flow.collectAsStateWithLifecycle().value
  }
}
