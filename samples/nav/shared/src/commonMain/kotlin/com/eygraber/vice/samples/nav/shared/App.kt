package com.eygraber.vice.samples.nav.shared

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.eygraber.vice.nav.viceComposable
import com.eygraber.vice.nav.viceDialog

@Composable
fun App() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = Routes.Home,
  ) {
    viceComposable<Routes.Home> {
      HomeDestination(
        onNavigateToCreateItem = { navController.navigate(Routes.Details.Create) },
        onNavigateToUpdateItem = { id -> navController.navigate(Routes.Details.Update(id)) },
        onNavigateToSettings = { navController.navigate(Routes.Settings) },
      )
    }

    viceDialog<Routes.Details.Create> {
      DetailsDestination(
        op = Routes.Details.Create,
        onNavigateBack = { navController.popBackStack() },
      )
    }

    viceDialog<Routes.Details.Update> { entry ->
      DetailsDestination(
        op = Routes.Details.Update(
          id = requireNotNull(entry.route.id),
        ),
        onNavigateBack = { navController.popBackStack() },
      )
    }

    navigation<Routes.Settings>(
      startDestination = Routes.Settings.Home,
    ) {
      viceComposable<Routes.Settings.Home> {
        SettingsDestination(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToAboutUs = {
            navController.navigate(Routes.Settings.AboutUs) {
              popUpTo(Routes.Settings.Home) {
                inclusive = true
                saveState = true
              }
            }
          },
        )
      }

      viceComposable<Routes.Settings.AboutUs> {
        AboutUsDestination(
          onNavigateBack = { navController.popBackStack() },
        )
      }
    }
  }
}
