package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.nav.ViceDestination

abstract class SampleDestination<I, C, S> : ViceDestination<I, C, ViceEffects, S>()
  where C : ViceCompositor<I, S>, I : Any, S : Any {
  override val effects = ViceEffects.None
}
