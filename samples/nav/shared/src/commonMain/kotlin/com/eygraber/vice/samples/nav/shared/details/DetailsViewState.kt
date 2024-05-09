package com.eygraber.vice.samples.nav.shared.details

data class DetailsViewState(
  val title: String,
  val titleError: String?,
  val description: String,
  val descriptionError: String?,
  val isDoneEnabled: Boolean,
)
