package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.samples.shared.details.DetailsCompositor
import com.eygraber.vice.samples.shared.details.DetailsIntent
import com.eygraber.vice.samples.shared.details.DetailsOp
import com.eygraber.vice.samples.shared.details.DetailsView
import com.eygraber.vice.samples.shared.details.DetailsViewState

class DetailsDestination(
  op: Routes.Details,
  onNavigateBack: () -> Unit,
) : SampleDestination<DetailsIntent, DetailsCompositor, DetailsViewState>() {
  override val view: DetailsView = { state, onIntent -> DetailsView(state, onIntent) }
  override val compositor = DetailsCompositor(
    op = when(op) {
      Routes.Details.Create -> DetailsOp.Create
      is Routes.Details.Update -> DetailsOp.Update(op.id)
    },
    onNavigateBack = onNavigateBack,
  )
}
