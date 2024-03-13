package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.flow.Flow

public abstract class DerivedStateSource<T> : ViceSource<T>, State<T> {
  protected abstract fun deriveState(): T

  private val state by lazy {
    derivedStateOf(::deriveState)
  }

  public val updates: Flow<T> get() = snapshotFlow { state.value }

  override val value: T get() = state.value

  @Composable
  @ReadOnlyComposable
  final override fun currentState(): T = state.value
}
