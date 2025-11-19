package com.eygraber.vice.nav3

import androidx.compose.runtime.remember
import androidx.navigation3.runtime.EntryProviderScope
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
import kotlin.jvm.JvmSuppressWildcards

@Suppress("FunctionName")
public fun <T, I, C, E, S> ViceNavEntry(
  key: T,
  viewProvider: () -> ViceView<I, S>,
  compositorProvider: () -> C,
  effectsProvider: () -> E,
  metadata: Map<String, Any> = emptyMap(),
  contentKey: Any = key.toString(),
  intentFiltersProvider: () -> List<ViceIntentFilter> = { listOf(ThrottlingIntentFilter()) },
  intentsProvider: () -> SharedFlow<I> = { MutableSharedFlow(extraBufferCapacity = 64) },
): NavEntry<T> where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any =
  NavEntry(
    key = key,
    contentKey = contentKey,
    metadata = metadata,
    content = {
      val view = remember(viewProvider) { viewProvider() }
      val compositor = remember(compositorProvider) { compositorProvider() }
      val effects = remember(effectsProvider) { effectsProvider() }
      val intentFilters = remember(intentFiltersProvider) { intentFiltersProvider() }
      val intents = remember(intentsProvider) { intentsProvider() }

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

public inline fun <reified T : Any> EntryProviderScope<in T>.viceEntry(
  crossinline entryProvider: (T) -> ViceNavEntryProvider<T, *, *, *, *>,
  noinline clazzContentKey: (key: @JvmSuppressWildcards T) -> Any = { it.toString() },
  metadata: Map<String, Any> = emptyMap(),
) {
  addEntryProvider(
    clazz = T::class,
    clazzContentKey = clazzContentKey,
    metadata = metadata,
    content = { key ->
      val entry = remember(key, metadata) {
        entryProvider(key).createEntry(key, metadata)
      }

      entry.Content()
    },
  )
}

public abstract class ViceNavEntryProvider<T, I, C, E, S>(
  @PublishedApi internal val intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  @PublishedApi internal val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  public abstract val view: ViceView<I, S>
  public abstract val compositor: C
  public abstract val effects: E

  public fun createEntry(
    key: T,
    contentKey: Any = key.toString(),
    metadata: Map<String, Any> = emptyMap(),
  ): NavEntry<T> = NavEntry(
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
}

public fun <T, I, C, E, S> EntryProviderScope<T>.viceEntry(
  key: T,
  viewProvider: () -> ViceView<I, S>,
  compositorProvider: () -> C,
  effectsProvider: () -> E,
  metadata: Map<String, Any> = emptyMap(),
  intentFiltersProvider: () -> List<ViceIntentFilter> = { listOf(ThrottlingIntentFilter()) },
  intentsProvider: () -> SharedFlow<I> = { MutableSharedFlow(extraBufferCapacity = 64) },
) where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  addEntryProvider(
    key = key,
    metadata = metadata,
    content = {
      val view = remember(viewProvider) { viewProvider() }
      val compositor = remember(compositorProvider) { compositorProvider() }
      val effects = remember(effectsProvider) { effectsProvider() }
      val intentFilters = remember(intentFiltersProvider) { intentFiltersProvider() }
      val intents = remember(intentsProvider) { intentsProvider() }

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
