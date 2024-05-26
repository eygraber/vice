package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.flow.Flow

@Stable
public abstract class MoleculeStateSource<T> : ViceSource<T> {
  private val state by lazy {
    mutableStateOf(initial)
  }

  public val updates: Flow<T> get() = snapshotFlow { state.value }

  public val value: T get() = state.value

  protected abstract val initial: T

  @Composable
  @ReadOnlyComposable
  protected abstract fun calculate(): T

  @Composable
  @ReadOnlyComposable
  private fun startCalculating() {
    state.value = calculate()
  }

  @Composable
  @ReadOnlyComposable
  final override fun currentState(): T {
    startCalculating()
    return state.value
  }
}
