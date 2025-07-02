package com.eygraber.vice.samples.shared.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eygraber.vice.samples.shared.TodoItem

typealias HomeView = @Composable (HomeViewState, (HomeIntent) -> Unit) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
  state: HomeViewState,
  onIntent: (HomeIntent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("Todo")
        },
        actions = {
          IconButton(
            onClick = { onIntent(HomeIntent.NavigateToSettings) },
          ) {
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
          }
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onIntent(HomeIntent.AddItem) },
      ) {
        Icon(Icons.Filled.Add, contentDescription = "Add new item")
      }
    },
  ) { contentPadding ->
    Box(
      modifier = Modifier
        .consumeWindowInsets(contentPadding)
        .padding(contentPadding)
        .fillMaxSize(),
    ) {
      LazyColumn {
        items(
          state.items,
          key = { it.id },
        ) { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onIntent(HomeIntent.NavigateToDetails(id = item.id))
              },
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Checkbox(
              checked = item.completed,
              onCheckedChange = { onIntent(HomeIntent.ToggleItemCompletion(item)) },
            )

            Spacer(Modifier.width(24.dp))

            Text(item.title)
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun TodoListPreview() {
  HomeView(
    state = HomeViewState(
      items = listOf(
        TodoItem(
          id = "one",
          completed = true,
          title = "Get milk",
        ),
      ),
    ),
    onIntent = {},
  )
}
