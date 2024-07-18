package com.eygraber.vice.portal

import androidx.compose.runtime.Composable
import com.eygraber.portal.compose.ComposePortal
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceContainer
import com.eygraber.vice.ViceEffects

public abstract class VicePortal<K, I, C, E, S> : ComposePortal<K>, ViceContainer<I, C, E, S>()
  where C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any {
  @Composable
  public final override fun Render() {
    Vice()
  }
}
