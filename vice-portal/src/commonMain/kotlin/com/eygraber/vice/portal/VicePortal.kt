package com.eygraber.vice.portal

import androidx.compose.runtime.Composable
import com.eygraber.portal.compose.ComposePortal
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceContainer
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.ViceView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

public abstract class VicePortal<K, V, I, C, E, S> : ComposePortal<K>, ViceContainer<V, I, C, E, S>
  where V : ViceView<I, S>, C : ViceCompositor<I, S>, E : ViceEffects {
  override val intents: SharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64)

  @Composable
  public final override fun Render() {
    Vice()
  }
}
