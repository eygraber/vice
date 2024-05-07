package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.flow.Flow

public abstract class FlowSource<T> : ViceSource<T> {
  protected abstract val flow: Flow<T>

  protected abstract val initial: T

  @Composable
  final override fun currentState(): T = flow.collectAsStateWithLifecycle(initial).value
}
