package com.eygraber.vice.samples.nav.shared.about

import com.eygraber.vice.samples.nav.shared.SampleDestination

class AboutUsDestination(
  onNavigateBack: () -> Unit,
) : SampleDestination<AboutUsView, AboutUsIntent, AboutUsCompositor, AboutUsViewState>() {
  override val view = AboutUsView()
  override val compositor = AboutUsCompositor(
    onNavigateBack,
  )
}
