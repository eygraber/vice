package com.eygraber.vice.samples.nav.shared.about

import com.eygraber.vice.samples.nav.shared.SampleDestination
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
class AboutUsDestination(
  onNavigateBack: () -> Unit,
) : SampleDestination<AboutUsIntent, AboutUsCompositor, AboutUsViewState>() {
  override val view: AboutUsView = { state, onIntent -> AboutUsView(state, onIntent) }
  override val compositor = AboutUsCompositor(
    onNavigateBack,
  )
}
