package com.eygraber.vice.samples.nav.shared.about

import com.eygraber.vice.samples.nav.shared.SampleDestination

class AboutUsDestination(
  onNavigateBack: () -> Unit,
) : SampleDestination<AboutUsIntent, AboutUsCompositor, AboutUsViewState>() {
  override val view: AboutUsView = { state, onIntent -> AboutUsView(state, onIntent) }
  override val compositor = AboutUsCompositor(
    onNavigateBack,
  )
}
