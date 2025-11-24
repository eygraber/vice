package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow

public abstract class MutableStateSource<T>(
  initializationThreadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) : StateSource<T> {
  private val state by lazy(initializationThreadSafetyMode) {
    mutableStateOf(initial)
  }

  public val updates: Flow<T> get() = snapshotFlow { state.value }

  override val value: T get() = state.value

  protected abstract val initial: T

  protected fun update(value: T) {
    state.value = value
  }

  @Composable
  @ReadOnlyComposable
  final override fun currentState(): T = state.value
}
