package com.eygraber.vice.samples.nav.shared.settings

import com.eygraber.vice.samples.nav.shared.SampleDestination

class SettingsDestination(
  onNavigateBack: () -> Unit,
  onNavigateToAboutUs: () -> Unit,
) : SampleDestination<SettingsView, SettingsIntent, SettingsCompositor, SettingsViewState>() {
  override val view = SettingsView()
  override val compositor = SettingsCompositor(
    onNavigateBack,
    onNavigateToAboutUs,
  )
}
