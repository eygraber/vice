package com.eygraber.vice.samples.nav.shared.settings

import androidx.compose.animation.AnimatedContentScope
import com.eygraber.vice.samples.nav.shared.SampleDestination

class SettingsDestination(
  onNavigateBack: () -> Unit,
  onNavigateToAboutUs: () -> Unit,
  scope: AnimatedContentScope
) : SampleDestination<SettingsView, SettingsIntent, SettingsCompositor, SettingsViewState>() {
  override val view = SettingsView(scope)
  override val compositor = SettingsCompositor(
    onNavigateBack,
    onNavigateToAboutUs,
  )
}
