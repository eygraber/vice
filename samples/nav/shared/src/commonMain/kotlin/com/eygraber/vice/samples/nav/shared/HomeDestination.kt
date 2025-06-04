package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.samples.shared.home.HomeCompositor
import com.eygraber.vice.samples.shared.home.HomeIntent
import com.eygraber.vice.samples.shared.home.HomeView
import com.eygraber.vice.samples.shared.home.HomeViewState

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
