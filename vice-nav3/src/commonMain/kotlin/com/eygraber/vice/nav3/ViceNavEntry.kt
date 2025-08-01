package com.eygraber.vice.nav3

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavEntry
import com.eygraber.vice.Vice
import com.eygraber.vice.ViceArgs
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.ViceView
import com.eygraber.vice.filter.ThrottlingIntentFilter
import com.eygraber.vice.filter.ViceIntentFilter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Suppress("FunctionName")
public fun <T, I, C, E, S> ViceNavEntry(
  key: T,
  view: ViceView<I, S>,
  compositor: C,
  effects: E,
  metadata: Map<String, Any> = emptyMap(),
  contentKey: Any = key.toString(),
  intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
): NavEntry<T> where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any =
  NavEntry(
    key = key,
    contentKey = contentKey,
    metadata = metadata,
    content = {
      Vice(
        ViceArgs(
          view = view,
          intents = intents as MutableSharedFlow<I>,
          compositor = compositor,
          intentFilters = intentFilters,
          effects = effects,
        ),
      )
    },
  )

public abstract class ViceNavEntryFactory<T, I, C, E, S>(
  @PublishedApi internal val intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  @PublishedApi internal val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  public abstract val view: ViceView<I, S>
  public abstract val compositor: C
  public abstract val effects: E

  public operator fun invoke(
    key: T,
    contentKey: Any = key.toString(),
    metadata: Map<String, Any> = emptyMap(),
  ): NavEntry<T> = ViceNavEntry(
    key = key,
    view = view,
    compositor = compositor,
    effects = effects,
    metadata = metadata,
    intentFilters = intentFilters,
    intents = intents,
  )
}

public fun <T : Any> EntryProviderBuilder<T>.viceEntry(
  key: T,
  factory: ViceNavEntryFactory<T, *, *, *, *>,
  metadata: Map<String, Any> = emptyMap(),
) {
  addEntryProvider(
    key = key,
    metadata = metadata,
    content = { key ->
      factory(key, metadata).Content()
    },
  )
}

public inline fun <reified T : Any> EntryProviderBuilder<*>.viceEntry(
  factory: ViceNavEntryFactory<T, *, *, *, *>,
  metadata: Map<String, Any> = emptyMap(),
) {
  addEntryProvider(
    clazz = T::class,
    clazzContentKey = { it.toString() },
    metadata = metadata,
    content = { key ->
      factory(key, metadata).Content()
    },
  )
}

public inline fun <reified T : Any> EntryProviderBuilder<*>.viceEntry(
  crossinline factoryProvider: (T) -> ViceNavEntryFactory<T, *, *, *, *>,
  noinline clazzContentKey: (key: @JvmSuppressWildcards T) -> Any = { it.toString() },
  metadata: Map<String, Any> = emptyMap(),
) {
  addEntryProvider(
    clazz = T::class,
    clazzContentKey = clazzContentKey,
    metadata = metadata,
    content = { key ->
      factoryProvider(key)(key, metadata).Content()
    },
  )
}

public fun <T, I, C, E, S> EntryProviderBuilder<T>.viceEntry(
  key: T,
  view: ViceView<I, S>,
  compositor: C,
  effects: E,
  metadata: Map<String, Any> = emptyMap(),
  intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  addEntryProvider(
    key = key,
    metadata = metadata,
    content = {
      Vice(
        ViceArgs(
          view = view,
          intents = intents as MutableSharedFlow<I>,
          compositor = compositor,
          intentFilters = intentFilters,
          effects = effects,
        ),
      )
    },
  )
}
