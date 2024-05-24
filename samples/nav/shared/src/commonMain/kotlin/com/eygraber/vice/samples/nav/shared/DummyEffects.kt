package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.ViceEffects
import kotlinx.coroutines.CoroutineScope

object DummyEffects : ViceEffects() {
  override fun CoroutineScope.onInitialized() {}
}
