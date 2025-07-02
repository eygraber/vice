package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.samples.shared.about.AboutUsCompositor
import com.eygraber.vice.samples.shared.about.AboutUsIntent
import com.eygraber.vice.samples.shared.about.AboutUsView
import com.eygraber.vice.samples.shared.about.AboutUsViewState

class AboutUsDestination(
  onNavigateBack: () -> Unit,
) : SampleDestination<AboutUsIntent, AboutUsCompositor, AboutUsViewState>() {
  override val view: AboutUsView = { state, onIntent -> AboutUsView(state, onIntent) }
  override val compositor = AboutUsCompositor(
    onNavigateBack,
  )
}
