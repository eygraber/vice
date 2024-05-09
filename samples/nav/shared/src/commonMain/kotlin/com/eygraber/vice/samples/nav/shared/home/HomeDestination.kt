package com.eygraber.vice.samples.nav.shared.home

import com.eygraber.vice.samples.nav.shared.SampleDestination

class HomeDestination(
  onNavigateToCreateItem: () -> Unit,
  onNavigateToUpdateItem: (String) -> Unit,
  onNavigateToSettings: () -> Unit,
) : SampleDestination<HomeView, HomeIntent, HomeCompositor, HomeViewState>() {
  override val view = HomeView()
  override val compositor = HomeCompositor(
    onNavigateToCreateItem,
    onNavigateToUpdateItem,
    onNavigateToSettings,
  )
}
