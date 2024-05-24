package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.nav.ViceDestination

abstract class SampleDestination<I, C, S> : ViceDestination<I, C, DummyEffects, S>()
  where C : ViceCompositor<I, S> {
  override val effects = DummyEffects
}
