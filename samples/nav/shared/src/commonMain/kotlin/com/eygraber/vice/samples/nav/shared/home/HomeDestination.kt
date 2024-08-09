package com.eygraber.vice.samples.nav.shared.home

import com.eygraber.vice.samples.nav.shared.SampleDestination
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
class HomeDestination(
  onNavigateToCreateItem: () -> Unit,
  onNavigateToUpdateItem: (String) -> Unit,
  onNavigateToSettings: () -> Unit,
) : SampleDestination<HomeIntent, HomeCompositor, HomeViewState>() {
  override val view: HomeView = { state, onIntent -> HomeView(state, onIntent) }

  override val compositor = HomeCompositor(
    onNavigateToCreateItem,
    onNavigateToUpdateItem,
    onNavigateToSettings,
  )
}
