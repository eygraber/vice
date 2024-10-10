package com.eygraber.vice.samples.nav.shared.settings

import com.eygraber.vice.samples.nav.shared.SampleDestination

class SettingsDestination(
  onNavigateBack: () -> Unit,
  onNavigateToAboutUs: () -> Unit,
) : SampleDestination<SettingsIntent, SettingsCompositor, SettingsViewState>() {
  override val view: SettingsView = { state, onIntent -> SettingsView(state, onIntent) }

  override val compositor = SettingsCompositor(
    onNavigateBack,
    onNavigateToAboutUs,
  )
}
