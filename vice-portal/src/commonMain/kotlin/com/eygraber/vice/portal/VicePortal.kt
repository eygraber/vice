package com.eygraber.vice.portal

import androidx.compose.runtime.Composable
import com.eygraber.portal.compose.ComposePortal
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceContainer
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.ViceView

public abstract class VicePortal<K, V, I, C, E, S> : ComposePortal<K>, ViceContainer<V, I, C, E, S>
  where V : ViceView<I, S>, C : ViceCompositor<I, S>, E : ViceEffects {
  @Composable
  public final override fun Render() {
    Vice()
  }
}
