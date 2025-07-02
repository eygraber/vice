package com.eygraber.vice.samples.nav3.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.nav3.ViceNavEntry
import com.eygraber.vice.samples.shared.about.AboutUsCompositor
import com.eygraber.vice.samples.shared.about.AboutUsView
import com.eygraber.vice.samples.shared.details.DetailsCompositor
import com.eygraber.vice.samples.shared.details.DetailsOp
import com.eygraber.vice.samples.shared.details.DetailsView
import com.eygraber.vice.samples.shared.home.HomeCompositor
import com.eygraber.vice.samples.shared.home.HomeView
import com.eygraber.vice.samples.shared.settings.SettingsCompositor
import com.eygraber.vice.samples.shared.settings.SettingsView

@Composable
fun App() {
  val backStack = rememberNavBackStack(Routes.Home)

  NavDisplay(
    backStack = backStack,
    entryProvider = { key ->
      when(key) {
        is Routes -> when(key) {
          Routes.Home -> ViceNavEntry(
            key = key,
            view = { state, onIntent -> HomeView(state, onIntent) },
            compositor = HomeCompositor(
              onNavigateToCreateItem = { backStack.add(Routes.Details.Create) },
              onNavigateToUpdateItem = { id -> backStack.add(Routes.Details.Update(id)) },
              onNavigateToSettings = { backStack.add(Routes.Settings.Home) },
            ),
            effects = ViceEffects.None,
          )

          is Routes.Details -> ViceNavEntry(
            key = key,
            view = { state, onIntent -> DetailsView(state, onIntent) },
            compositor = DetailsCompositor(
              op = when(key) {
                Routes.Details.Create -> DetailsOp.Create
                is Routes.Details.Update -> DetailsOp.Update(key.id)
              },
              onNavigateBack = backStack::removeLastOrNull,
            ),
            effects = ViceEffects.None,
          )

          Routes.Settings.Home -> ViceNavEntry(
            key = Routes.Settings.Home,
            view = { state, onIntent -> SettingsView(state, onIntent) },
            compositor = SettingsCompositor(
              onNavigateBack = backStack::removeLastOrNull,
              onNavigateToAboutUs = {
                while(true) {
                  if(backStack.removeLastOrNull() is Routes.Settings.Home) {
                    break
                  }
                }
                backStack.add(Routes.Settings.AboutUs)
              },
            ),
            effects = ViceEffects.None,
          )

          Routes.Settings.AboutUs -> ViceNavEntry(
            key = Routes.Settings.AboutUs,
            view = { state, onIntent -> AboutUsView(state, onIntent) },
            compositor = AboutUsCompositor(backStack::removeLastOrNull),
            effects = ViceEffects.None,
          )
        }

        else -> NavEntry(key) {
          Text("Invalid Key: $key")
        }
      }
    },
  )
}
