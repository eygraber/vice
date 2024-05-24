package com.eygraber.vice.samples.nav.shared.settings

import androidx.compose.runtime.Composable
import com.eygraber.vice.ViceCompositor

class SettingsCompositor(
  private val onNavigateBack: () -> Unit,
  private val onNavigateToAboutUs: () -> Unit,
) : ViceCompositor<SettingsIntent, SettingsViewState>() {
  @Composable
  override fun composite() = SettingsViewState

  override suspend fun onIntent(intent: SettingsIntent) {
    when(intent) {
      SettingsIntent.Close -> onNavigateBack()
      SettingsIntent.NavigateToAboutUs -> onNavigateToAboutUs()
    }
  }
}
