package com.eygraber.vice.samples.nav.shared

import kotlinx.serialization.Serializable

sealed interface Routes {
  @Serializable
  data object Home : Routes

  sealed interface Details : Routes {
    @Serializable
    data object Create : Details

    @Serializable
    data class Update(val id: String) : Details
  }

  @Serializable
  data object Settings : Routes {
    @Serializable
    data object Home : Routes

    @Serializable
    data object AboutUs : Routes
  }
}
