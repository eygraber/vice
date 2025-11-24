package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

public abstract class SerializableMutableStateSource<T : Any>(
  private val stateSerializer: KSerializer<T>,
  initializationThreadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) : StateSource<T> {
  private val stateOfState by lazy(initializationThreadSafetyMode) {
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
    val rememberedState = rememberSerializable(stateSerializer = stateSerializer) {
      stateOfState.value
    }
    stateOfState.value = rememberedState
    return stateOfState.value.value
  }
}
