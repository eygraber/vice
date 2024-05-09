package com.eygraber.vice.samples.nav.desktop

import androidx.compose.ui.window.singleWindowApplication
import com.eygraber.vice.samples.nav.shared.App

fun main() {
  // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  singleWindowApplication(title = "Nav") {
    App()
  }
}
