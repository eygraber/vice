package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import com.eygraber.vice.ViceSource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

public interface MutableViceSource<T> : ViceSource<T> {
  public fun update(value: T)
}

public fun <T> mutableViceSource(
  initial: T,
): MutableViceSource<T> = object : MutableViceSource<T> {
  private var state by mutableStateOf(initial)

  override fun update(value: T) {
    state = value
  }

  @Composable
  override fun currentState(): T = state
}

public fun <T> saveableMutableViceSource(
  initial: T,
  saver: Saver<T, out Any>? = null,
): MutableViceSource<T> = object : MutableViceSource<T> {
  private var state = mutableStateOf(initial)

  override fun update(value: T) {
    state.value = value
  }

  @Composable
  override fun currentState(): T {
    state = when(saver) {
      null -> rememberSaveable { state }
      else -> rememberSaveable(stateSaver = saver) { state }
    }

    return state.value
  }
}

@OptIn(InternalSerializationApi::class)
public inline fun <reified T : Any> serializableMutableViceSource(
  initial: T,
  serializer: KSerializer<T> = T::class.serializer(),
): MutableViceSource<T> = object : MutableViceSource<T> {
  private var state = mutableStateOf(initial)

  override fun update(value: T) {
    state.value = value
  }

  @Composable
  override fun currentState(): T {
    state = rememberSerializable(stateSerializer = serializer) {
      state
    }

    return state.value
  }
}

public fun mutableDoubleSource(
  initial: Double,
  isSaveable: Boolean = false,
): MutableViceSource<Double> = object : MutableViceSource<Double> {
  private var state = mutableDoubleStateOf(initial)

  override fun update(value: Double) {
    state.doubleValue = value
  }

  @Composable
  override fun currentState(): Double =
    if(isSaveable) {
      state = rememberSaveable { state }
      state.doubleValue
    }
    else {
      state.doubleValue
    }
}

public fun mutableFloatSource(
  initial: Float,
  isSaveable: Boolean = false,
): MutableViceSource<Float> = object : MutableViceSource<Float> {
  private var state = mutableFloatStateOf(initial)

  override fun update(value: Float) {
    state.floatValue = value
  }

  @Composable
  override fun currentState(): Float =
    if(isSaveable) {
      state = rememberSaveable { state }
      state.floatValue
    }
    else {
      state.floatValue
    }
}

public fun mutableIntSource(
  initial: Int,
  isSaveable: Boolean = false,
): MutableViceSource<Int> = object : MutableViceSource<Int> {
  private var state = mutableIntStateOf(initial)

  override fun update(value: Int) {
    state.intValue = value
  }

  @Composable
  override fun currentState(): Int =
    if(isSaveable) {
      state = rememberSaveable { state }
      state.intValue
    }
    else {
      state.intValue
    }
}

public fun mutableLongSource(
  initial: Long,
  isSaveable: Boolean = false,
): MutableViceSource<Long> = object : MutableViceSource<Long> {
  private var state = mutableLongStateOf(initial)

  override fun update(value: Long) {
    state.longValue = value
  }

  @Composable
  override fun currentState(): Long =
    if(isSaveable) {
      state = rememberSaveable { state }
      state.longValue
    }
    else {
      state.longValue
    }
}
