package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.flow.Flow

public abstract class MoleculeStateSource<T> : ViceSource<T>, State<T> {
  private val state by lazy {
    mutableStateOf(initial)
  }

  public val updates: Flow<T> get() = snapshotFlow { state.value }

  override val value: T get() = state.value

  protected abstract val initial: T

  @Composable
  @ReadOnlyComposable
  protected abstract fun calculate(): T

  @Composable
  @ReadOnlyComposable
  final override fun currentState(): T {
    state.value = calculate()
    return state.value
  }
}
