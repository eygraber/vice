package com.eygraber.vice.samples.nav.shared

import com.eygraber.vice.samples.shared.settings.SettingsCompositor
import com.eygraber.vice.samples.shared.settings.SettingsIntent
import com.eygraber.vice.samples.shared.settings.SettingsView
import com.eygraber.vice.samples.shared.settings.SettingsViewState

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
