package com.eygraber.vice.samples.nav.shared

import androidx.compose.runtime.Composable
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceView
import com.eygraber.vice.nav.ViceDestination

abstract class SampleDestination<V, I, C, S> : ViceDestination<V, I, C, DummyEffects, S>()
  where V : ViceView<I, S>, C : ViceCompositor<I, S> {
  override val effects = DummyEffects
  @Composable final override fun OnBackPressedHandler(enabled: Boolean, onBackPressed: () -> Unit) {}
}
