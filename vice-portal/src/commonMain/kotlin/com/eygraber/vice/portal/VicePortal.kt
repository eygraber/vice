// needed to access internal members of vice-core
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.eygraber.vice.portal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.eygraber.portal.compose.ComposePortal
import com.eygraber.vice.Launch
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.ViceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

public abstract class VicePortal<K, V, I, C, E, S> : ComposePortal<K>
  where V : ViceView<I, S>, C : ViceCompositor<I, S>, E : ViceEffects {
  protected abstract val view: V
  private val intents = MutableSharedFlow<I>(extraBufferCapacity = 64)
  protected abstract val compositor: C
  protected abstract val effects: E

  @Composable
  protected abstract fun OnBackPressedHandler(enabled: Boolean, onBackPressed: () -> Unit)

  @Composable
  public final override fun Render() {
    val scope = rememberCoroutineScope {
      Dispatchers.Main.immediate
    }

    OnBackPressedHandler(enabled = compositor.internalIsBackHandlerEnabled()) {
      compositor.internalOnBackPressed { intent ->
        // this is synchronous because the dispatcher is Main.immediate
        scope.launch {
          compositor.internalOnIntent(intent)
        }

        intents.tryEmit(intent)
      }
    }

    Render(
      view as ViceView<I, S>,
      intents,
      compositor,
      effects,
      scope,
    )
  }
}

@Suppress("NOTHING_TO_INLINE")
@Composable
private inline fun <I, S> Render(
  view: ViceView<I, S>,
  intents: SharedFlow<I>,
  compositor: ViceCompositor<I, S>,
  effects: ViceEffects,
  scope: CoroutineScope,
) {
  effects.Launch()

  val state = compositor.internalComposite(intents)
  val intentHandler: (I) -> Unit = remember(scope, compositor, intents) {
    { intent: I ->
      // this is synchronous because the dispatcher is Main.immediate
      scope.launch {
        compositor.internalOnIntent(intent)
      }

      (intents as MutableSharedFlow<I>).tryEmit(intent)
    }
  }

  view.Render(state, intentHandler)
}
