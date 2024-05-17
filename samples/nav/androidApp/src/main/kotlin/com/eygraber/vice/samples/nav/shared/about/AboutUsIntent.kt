package com.eygraber.vice.samples.nav.shared.about

sealed interface AboutUsIntent {
  data object Close : AboutUsIntent
}
