package com.eygraber.vice.nav3

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
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
  intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
): NavEntry<T> where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any =
  NavEntry(
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

public abstract class ViceNavEntryFactory<T, I, C, E, S>(
  internal val intentFilters: List<ViceIntentFilter> = listOf(ThrottlingIntentFilter()),
  internal val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where T : Any, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  public abstract val view: ViceView<I, S>
  public abstract val compositor: C
  public abstract val effects: E

  public operator fun invoke(
    key: T,
    metadata: Map<String, Any>,
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

public fun <T, I, C, E, S> EntryProviderBuilder<T>.viceEntry(
  key: T,
  factory: ViceNavEntryFactory<T, I, C, E, S>,
  metadata: Map<String, Any> = emptyMap(),
) where T : NavKey, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  addEntryProvider(
    key = key,
    metadata = metadata,
    content = {
      Vice(
        ViceArgs(
          view = factory.view,
          intents = factory.intents as MutableSharedFlow<I>,
          compositor = factory.compositor,
          intentFilters = factory.intentFilters,
          effects = factory.effects,
        ),
      )
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
) where T : NavKey, C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
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
