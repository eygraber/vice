package com.eygraber.vice.samples.nav.shared.about

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eygraber.vice.ViceView

class AboutUsView : ViceView<AboutUsIntent, AboutUsViewState> {
  @Composable
  override fun Render(state: AboutUsViewState, onIntent: (AboutUsIntent) -> Unit) {
    AboutUs(state, onIntent)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutUs(
  @Suppress("UNUSED_PARAMETER") state: AboutUsViewState,
  onIntent: (AboutUsIntent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("About Us")
        },
        navigationIcon = {
          IconButton(
            onClick = { onIntent(AboutUsIntent.Close) },
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
      Text("This is the about us page")
    }
  }
}

@Preview
@Composable
private fun AboutUsPreview() {
  AboutUs(
    state = AboutUsViewState,
    onIntent = {},
  )
}
