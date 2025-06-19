package com.eygraber.vice.samples.shared.about

import androidx.compose.runtime.Composable
import com.eygraber.vice.ViceCompositor

class AboutUsCompositor(
  private val onNavigateBack: () -> Unit,
) : ViceCompositor<AboutUsIntent, AboutUsViewState> {
  @Composable
  override fun composite() = AboutUsViewState

  override suspend fun onIntent(intent: AboutUsIntent) {
    when(intent) {
      AboutUsIntent.Close -> onNavigateBack()
    }
  }
}
