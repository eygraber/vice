package com.eygraber.vice.samples.nav.shared.home

import androidx.compose.animation.AnimatedContentScope
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
import com.eygraber.vice.ViceView
import com.eygraber.vice.samples.nav.shared.virtueSharedBounds
import com.eygraber.vice.samples.nav.shared.virtueSharedElement

class HomeView(private val animatedContentScope: AnimatedContentScope) : ViceView<HomeIntent, HomeViewState> {
  @Composable
  override fun Render(state: HomeViewState, onIntent: (HomeIntent) -> Unit) {
    animatedContentScope.TodoList(state, onIntent)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedContentScope.TodoList(
  state: HomeViewState,
  onIntent: (HomeIntent) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.virtueSharedBounds(
          "topBar",
          this
        ),
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
