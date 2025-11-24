package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
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
  @ReadOnlyComposable
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
): MutableViceSource<Double> = object : MutableViceSource<Double> {
  private var state by mutableDoubleStateOf(initial)

  override fun update(value: Double) {
    state = value
  }

  @Composable
  @ReadOnlyComposable
  override fun currentState(): Double = state
}

public fun saveableMutableDoubleSource(
  initial: Double,
): MutableViceSource<Double> = object : MutableViceSource<Double> {
  private var state = mutableDoubleStateOf(initial)

  override fun update(value: Double) {
    state.doubleValue = value
  }

  @Composable
  override fun currentState(): Double {
    state = rememberSaveable { state }
    return state.doubleValue
  }
}

public fun mutableFloatSource(
  initial: Float,
): MutableViceSource<Float> = object : MutableViceSource<Float> {
  private var state by mutableFloatStateOf(initial)

  override fun update(value: Float) {
    state = value
  }

  @Composable
  @ReadOnlyComposable
  override fun currentState(): Float = state
}

public fun saveableMutableFloatSource(
  initial: Float,
): MutableViceSource<Float> = object : MutableViceSource<Float> {
  private var state = mutableFloatStateOf(initial)

  override fun update(value: Float) {
    state.floatValue = value
  }

  @Composable
  override fun currentState(): Float {
    state = rememberSaveable { state }
    return state.floatValue
  }
}

public fun mutableIntSource(
  initial: Int,
): MutableViceSource<Int> = object : MutableViceSource<Int> {
  private var state by mutableIntStateOf(initial)

  override fun update(value: Int) {
    state = value
  }

  @Composable
  @ReadOnlyComposable
  override fun currentState(): Int = state
}

public fun saveableMutableIntSource(
  initial: Int,
): MutableViceSource<Int> = object : MutableViceSource<Int> {
  private var state = mutableIntStateOf(initial)

  override fun update(value: Int) {
    state.intValue = value
  }

  @Composable
  override fun currentState(): Int {
    state = rememberSaveable { state }
    return state.intValue
  }
}

public fun mutableLongSource(
  initial: Long,
): MutableViceSource<Long> = object : MutableViceSource<Long> {
  private var state by mutableLongStateOf(initial)

  override fun update(value: Long) {
    state = value
  }

  @Composable
  @ReadOnlyComposable
  override fun currentState(): Long = state
}

public fun saveableMutableLongSource(
  initial: Long,
): MutableViceSource<Long> = object : MutableViceSource<Long> {
  private var state = mutableLongStateOf(initial)

  override fun update(value: Long) {
    state.longValue = value
  }

  @Composable
  override fun currentState(): Long {
    state = rememberSaveable { state }
    return state.longValue
  }
}
