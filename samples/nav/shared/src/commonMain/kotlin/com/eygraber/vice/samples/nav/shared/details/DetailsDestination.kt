package com.eygraber.vice.samples.nav.shared.details

import com.eygraber.vice.samples.nav.shared.Routes
import com.eygraber.vice.samples.nav.shared.SampleDestination
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
class DetailsDestination(
  op: Routes.Details,
  onNavigateBack: () -> Unit,
) : SampleDestination<DetailsIntent, DetailsCompositor, DetailsViewState>() {
  override val view: DetailsView = { state, onIntent -> DetailsView(state, onIntent) }
  override val compositor = DetailsCompositor(
    op,
    onNavigateBack,
  )
}
