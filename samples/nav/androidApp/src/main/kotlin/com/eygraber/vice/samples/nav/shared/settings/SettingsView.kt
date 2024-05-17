package com.eygraber.vice.samples.nav.shared.settings

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eygraber.vice.ViceView
import com.eygraber.vice.samples.nav.shared.virtueSharedBounds
import com.eygraber.vice.samples.nav.shared.virtueSharedElement

class SettingsView(
  private val animatedContentScope: AnimatedContentScope
) : ViceView<SettingsIntent, SettingsViewState> {
  @Composable
  override fun Render(state: SettingsViewState, onIntent: (SettingsIntent) -> Unit) {
    animatedContentScope.Settings(state, onIntent)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedContentScope.Settings(
  @Suppress("UNUSED_PARAMETER") state: SettingsViewState,
  onIntent: (SettingsIntent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.virtueSharedBounds(
          "topBar",
          this@Settings
        ),
        title = {
          Text("Settings")
        },
        navigationIcon = {
          IconButton(
            onClick = { onIntent(SettingsIntent.Close) },
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
          }
        },
      )
    },
  ) { contentPadding ->
    Column(
      modifier = Modifier
        .consumeWindowInsets(contentPadding)
        .padding(contentPadding)
        .fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text("This is the settings page")

      Spacer(Modifier.height(8.dp))

      TextButton(
        onClick = { onIntent(SettingsIntent.NavigateToAboutUs) },
      ) {
        Text("About Us")
      }
    }
  }
}
