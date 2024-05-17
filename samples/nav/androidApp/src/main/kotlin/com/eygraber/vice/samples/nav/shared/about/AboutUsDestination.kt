package com.eygraber.vice.samples.nav.shared.about

import androidx.compose.animation.AnimatedContentScope
import com.eygraber.vice.samples.nav.shared.SampleDestination

class AboutUsDestination(
  onNavigateBack: () -> Unit,
  scope: AnimatedContentScope,
) : SampleDestination<AboutUsView, AboutUsIntent, AboutUsCompositor, AboutUsViewState>() {
  override val view = AboutUsView(scope)
  override val compositor = AboutUsCompositor(
    onNavigateBack,
  )
}
