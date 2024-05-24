package com.eygraber.vice.samples.nav.shared.settings

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eygraber.vice.ViceView

class SettingsView : ViceView<SettingsIntent, SettingsViewState> {
  @Composable
  override fun Render(state: SettingsViewState, onIntent: (SettingsIntent) -> Unit) {
    Settings(state, onIntent)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
  @Suppress("UNUSED_PARAMETER") state: SettingsViewState,
  onIntent: (SettingsIntent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
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

@Preview
@Composable
private fun SettingsPreview() {
  Settings(
    state = SettingsViewState,
    onIntent = {},
  )
}
