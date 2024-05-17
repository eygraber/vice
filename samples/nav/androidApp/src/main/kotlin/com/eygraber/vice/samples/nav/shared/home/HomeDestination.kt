package com.eygraber.vice.samples.nav.shared.home

import androidx.compose.animation.AnimatedContentScope
import com.eygraber.vice.samples.nav.shared.SampleDestination

class HomeDestination(
  onNavigateToCreateItem: () -> Unit,
  onNavigateToUpdateItem: (String) -> Unit,
  onNavigateToSettings: () -> Unit,
  scope: AnimatedContentScope
) : SampleDestination<HomeView, HomeIntent, HomeCompositor, HomeViewState>() {
  override val view = HomeView(scope)
  override val compositor = HomeCompositor(
    onNavigateToCreateItem,
    onNavigateToUpdateItem,
    onNavigateToSettings,
  )
}
