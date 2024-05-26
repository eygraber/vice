package com.eygraber.vice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

public abstract class ViceContainer<I, C, E, S>(
  private val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where C : ViceCompositor<I, S>, E : ViceEffects {

  public abstract val view: ViceView<I, S>
  public abstract val compositor: C
  public abstract val effects: E

  @Composable
  public fun Vice() {
    RunVice(
      view = view,
      intents = intents as MutableSharedFlow<I>,
      compositor = compositor,
      effects = effects,
    )
  }
}

@Composable
private fun <I, S> RunVice(
  view: ViceView<I, S>,
  intents: MutableSharedFlow<I>,
  compositor: ViceCompositor<I, S>,
  effects: ViceEffects,
) {
  val scope = rememberCoroutineScope {
    Dispatchers.Main.immediate
  }

  ViceUdf(
    view,
    intents,
    compositor,
    effects,
    scope,
  )
}

@Suppress("NOTHING_TO_INLINE")
@Composable
private inline fun <I, S> ViceUdf(
  view: ViceView<I, S>,
  intents: SharedFlow<I>,
  compositor: ViceCompositor<I, S>,
  effects: ViceEffects,
  scope: CoroutineScope,
) {
  effects.Launch()

  val state = compositor.composite()
  val intentHandler: (I) -> Unit = remember(scope, compositor, intents) {
    { intent: I ->
      // this is synchronous because the dispatcher is Main.immediate
      // (should be able to get rid of this once BTF2 is in Material)
      scope.launch {
        compositor.onIntent(intent)
      }

      (intents as MutableSharedFlow<I>).tryEmit(intent)
    }
  }

  view(state, intentHandler)
}
