package com.eygraber.vice.samples.nav.shared.details

sealed interface DetailsIntent {
  data object NavigateBack : DetailsIntent

  data class TitleUpdated(val title: String) : DetailsIntent
  data class DescriptionUpdated(val description: String) : DetailsIntent

  data class Done(val title: String, val description: String) : DetailsIntent
}
