package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow

public abstract class SaveableMutableStateSource<T : Any>(
  private val saver: Saver<T, out Any>? = null,
) : StateSource<T> {
  private val stateOfState by lazy {
    mutableStateOf(mutableStateOf(initial))
  }

  public val updates: Flow<T> get() = snapshotFlow { stateOfState.value.value }

  override val value: T get() = stateOfState.value.value

  protected abstract val initial: T

  protected fun update(value: T) {
    stateOfState.value.value = value
  }

  @Composable
  override fun currentState(): T {
    val rememberedState = when(saver) {
      null -> rememberSaveable { stateOfState.value }
      else -> rememberSaveable(stateSaver = saver) { stateOfState.value }
    }
    stateOfState.value = rememberedState
    return stateOfState.value.value
  }
}
