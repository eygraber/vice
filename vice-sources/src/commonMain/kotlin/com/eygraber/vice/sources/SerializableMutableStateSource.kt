package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

public abstract class SerializableMutableStateSource<T : Any>(
  private val stateSerializer: KSerializer<T>,
) : StateSource<T> {
  private var stateOfState by mutableStateOf(mutableStateOf(initial))

  public val updates: Flow<T> get() = snapshotFlow { stateOfState.value }

  override val value: T get() = stateOfState.value

  protected abstract val initial: T

  protected fun update(value: T) {
    stateOfState.value = value
  }

  @Composable
  override fun currentState(): T {
    val rememberedState = rememberSerializable(stateSerializer = stateSerializer) {
      mutableStateOf(initial)
    }
    stateOfState = rememberedState
    return stateOfState.value
  }
}
