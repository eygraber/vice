package com.eygraber.vice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.eygraber.vice.filter.ThrottlingIntentFilter
import com.eygraber.vice.filter.ViceIntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

public abstract class ViceContainer<I, C, E, S>(
  vararg intentFilters: ViceIntentFilter = arrayOf(ThrottlingIntentFilter()),
  private val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64),
) where C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  private val intentFilters = intentFilters.toList()

  public abstract val view: ViceView<I, S>
  public abstract val compositor: C
  public abstract val effects: E

  @Composable
  public fun Vice() {
    RunVice(
      ViceArgs(
        view = view,
        intents = intents as MutableSharedFlow<I>,
        compositor = compositor,
        intentFilters = intentFilters,
        effects = effects,
      ),
    )
  }
}

@Stable
private data class ViceArgs<I : Any, S : Any>(
  val view: ViceView<I, S>,
  val intents: MutableSharedFlow<I>,
  val compositor: ViceCompositor<I, S>,
  val intentFilters: List<ViceIntentFilter>,
  val effects: ViceEffects,
  val scope: CoroutineScope? = null,
)

@Composable
private fun <I : Any, S : Any> RunVice(
  args: ViceArgs<I, S>,
) {
  val scope = rememberCoroutineScope {
    Dispatchers.Main.immediate
  }

  ViceUdf(
    args.copy(scope = scope),
  )
}

@Composable
private fun <I : Any, S : Any> ViceUdf(
  args: ViceArgs<I, S>,
) {
  args.effects.Launch()

  val state = args.compositor.composite()
  val intentHandler: (I) -> Unit = remember(args.intentFilters, args.scope, args.compositor, args.intents) {
    { intent: I ->
      if(args.intentFilters.all { it.filter(intent) }) {
        // this is synchronous because the dispatcher is Main.immediate
        // (should be able to get rid of this once BTF2 is in Material)
        args.scope?.launch {
          args.compositor.onIntent(intent)
        }

        args.intents.tryEmit(intent)
      }
    }
  }

  args.view(state, intentHandler)
}
