package com.eygraber.vice.samples.nav.shared.settings

sealed interface SettingsIntent {
  data object Close : SettingsIntent
  data object NavigateToAboutUs : SettingsIntent
}
