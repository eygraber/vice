package com.eygraber.vice.samples.nav.shared

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eygraber.vice.nav.viceComposable
import com.eygraber.vice.nav.viceDialog
import com.eygraber.vice.samples.nav.shared.about.AboutUsDestination
import com.eygraber.vice.samples.nav.shared.details.DetailsDestination
import com.eygraber.vice.samples.nav.shared.home.HomeDestination
import com.eygraber.vice.samples.nav.shared.settings.SettingsDestination

@Composable
fun App() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "/todo",
  ) {
    viceComposable(
      route = "/todo",
    ) {
      HomeDestination(
        onNavigateToCreateItem = { navController.navigate("/todo/create") },
        onNavigateToUpdateItem = { id -> navController.navigate("/todo/update/$id") },
        onNavigateToSettings = { navController.navigate("/settings") },
      )
    }

    viceDialog(
      route = "/todo/create",
    ) {
      DetailsDestination(
        op = Routes.Details.Create,
        onNavigateBack = { navController.popBackStack() },
      )
    }

    viceDialog(
      route = "/todo/update/{id}",
      arguments = listOf(
        navArgument("id") { type = NavType.StringType },
      ),
    ) { op ->
      DetailsDestination(
        op = Routes.Details.Update(
          id = requireNotNull(op.arguments?.getString("id")),
        ),
        onNavigateBack = { navController.popBackStack() },
      )
    }

    navigation(
      route = "/settings-home",
      startDestination = "/settings",
    ) {
      viceComposable(
        route = "/settings",
      ) {
        SettingsDestination(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToAboutUs = {
            navController.navigate("/settings/about-us") {
              popUpTo("/settings") {
                inclusive = true
                saveState = true
              }
            }
          },
        )
      }

      viceComposable(
        route = "/settings/about-us",
      ) {
        AboutUsDestination(
          onNavigateBack = { navController.popBackStack() },
        )
      }
    }
  }
}
