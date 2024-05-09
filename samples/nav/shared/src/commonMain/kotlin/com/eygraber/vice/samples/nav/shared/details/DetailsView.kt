package com.eygraber.vice.samples.nav.shared.details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eygraber.vice.ViceView

class DetailsView : ViceView<DetailsIntent, DetailsViewState> {
  @Composable
  override fun Render(state: DetailsViewState, onIntent: (DetailsIntent) -> Unit) {
    DetailsView(state, onIntent)
  }
}

@Composable
private fun DetailsView(
  state: DetailsViewState,
  onIntent: (DetailsIntent) -> Unit,
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    LazyColumn {
      item {
        TextField(
          value = state.title,
          onValueChange = { newValue ->
            onIntent(DetailsIntent.TitleUpdated(newValue))
          },
          isError = state.titleError != null,
          label = {
            Text(
              when(state.titleError) {
                null -> "Title"
                else -> state.titleError
              },
            )
          },
        )
      }

      item {
        TextField(
          value = state.description,
          onValueChange = { newValue ->
            onIntent(DetailsIntent.DescriptionUpdated(newValue))
          },
          isError = state.descriptionError != null,
          label = {
            Text(
              when(state.descriptionError) {
                null -> "Description"
                else -> state.descriptionError
              },
            )
          },
        )
      }

      item {
        Button(
          onClick = { onIntent(DetailsIntent.Done(state.title, state.description)) },
          enabled = state.isDoneEnabled,
        ) {
          Text("Done")
        }
      }
    }
  }
}

@Preview
@Composable
private fun DetailsViewPreview() {
  DetailsView(
    DetailsViewState(
      title = "This is a title",
      titleError = null,
      description = "This is a description",
      descriptionError = null,
      isDoneEnabled = true,
    ),
  ) {}
}
