package com.eygraber.vice.samples.shared.about

sealed interface AboutUsIntent {
  data object Close : AboutUsIntent
}
