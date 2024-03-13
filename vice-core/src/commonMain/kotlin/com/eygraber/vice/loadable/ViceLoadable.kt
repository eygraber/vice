package com.eygraber.vice.loadable

import androidx.compose.runtime.Immutable

@Immutable
public sealed interface ViceLoadable<T> {
  public val value: T

  @Immutable
  public data class Loading<T>(override val value: T) : ViceLoadable<T>

  @Immutable
  public data class Loaded<T>(override val value: T) : ViceLoadable<T>
}

public inline val ViceLoadable<*>.isLoading: Boolean get() = this is ViceLoadable.Loading
public inline val ViceLoadable<*>.isNotLoading: Boolean get() = this !is ViceLoadable.Loading
public inline val ViceLoadable<*>.isLoaded: Boolean get() = this is ViceLoadable.Loaded
public inline val ViceLoadable<*>.isNotLoaded: Boolean get() = this !is ViceLoadable.Loaded

public inline val <T> ViceLoadable<T>.loadedValueOrNull: T? get() = when(this) {
  is ViceLoadable.Loading -> null
  is ViceLoadable.Loaded -> value
}

public inline fun <T, R> ViceLoadable<T>.map(
  mapper: (T) -> ViceLoadable<R>,
): ViceLoadable<R> = mapper(value)

public inline fun <T, R> ViceLoadable<T>.mapValue(
  mapper: (T) -> R,
): ViceLoadable<R> = when(this) {
  is ViceLoadable.Loading -> ViceLoadable.Loading(mapper(value))
  is ViceLoadable.Loaded -> ViceLoadable.Loaded(mapper(value))
}
