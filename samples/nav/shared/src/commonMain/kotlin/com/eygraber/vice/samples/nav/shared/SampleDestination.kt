package com.eygraber.vice.samples.nav.shared

import androidx.compose.runtime.Composable
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.nav.ViceDestination

abstract class SampleDestination<I, C, S> : ViceDestination<I, C, DummyEffects, S>()
  where C : ViceCompositor<I, S> {
  override val effects = DummyEffects
  @Composable final override fun OnBackPressedHandler(enabled: Boolean, onBackPressed: () -> Unit) {}
}
