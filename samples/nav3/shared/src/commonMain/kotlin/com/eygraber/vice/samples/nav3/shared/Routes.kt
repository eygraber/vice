package com.eygraber.vice.samples.nav3.shared

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
  @Serializable
  data object Home : Routes

  @Serializable
  sealed interface Details : Routes {
    @Serializable
    data object Create : Details

    @Serializable
    data class Update(val id: String) : Details
  }

  @Serializable
  sealed interface Settings {
    @Serializable
    data object Home : Routes

    @Serializable
    data object AboutUs : Routes
  }
}
