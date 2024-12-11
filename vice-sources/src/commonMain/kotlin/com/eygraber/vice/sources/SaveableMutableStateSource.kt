package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import com.eygraber.vice.ViceSource
import kotlinx.coroutines.flow.Flow

public abstract class SaveableMutableStateSource<T : Any>(
  private val saver: Saver<T, out Any>? = null,
) : ViceSource<T>, State<T> {
  private val state by lazy {
    mutableStateOf(initial)
  }

  public val updates: Flow<T> get() = snapshotFlow { state.value }

  override val value: T get() = state.value

  protected abstract val initial: T

  protected fun update(value: T) {
    state.value = value
  }

  @Composable
  override fun currentState(): T = when(saver) {
    null -> rememberSaveable { state }.value
    else -> rememberSaveable(stateSaver = saver) { state }.value
  }
}
