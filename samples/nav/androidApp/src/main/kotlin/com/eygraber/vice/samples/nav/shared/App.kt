package com.eygraber.vice.samples.nav.shared

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.eygraber.vice.nav.viceComposable
import com.eygraber.vice.nav.viceDialog
import com.eygraber.vice.samples.nav.shared.about.AboutUsDestination
import com.eygraber.vice.samples.nav.shared.details.DetailsDestination
import com.eygraber.vice.samples.nav.shared.home.HomeDestination
import com.eygraber.vice.samples.nav.shared.settings.SettingsDestination

@OptIn(ExperimentalSharedTransitionApi::class)
data class SharedTransitionScopes(
  val shared: SharedTransitionScope?,
  val animated: AnimatedContentScope?
) {
  companion object {
    val Default = SharedTransitionScopes(null, null)
  }
}

val LocalSharedTransitionScopes = compositionLocalOf { SharedTransitionScopes.Default }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.virtueSharedBounds(
  key: Any,
  animated: AnimatedContentScope
): Modifier = then(
  LocalSharedTransitionScopes.current.shared?.run {
    Modifier.sharedBounds(
      rememberSharedContentState(key),
      animated
    )
  } ?: Modifier
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.virtueSharedElement(
  key: Any,
  animated: AnimatedContentScope
): Modifier = then(
  LocalSharedTransitionScopes.current.shared?.run {
    Modifier.sharedElement(
      rememberSharedContentState(key),
      animated
    )
  } ?: Modifier
)

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
  val navController = rememberNavController()

  Box(
    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
  ) {
    SharedTransitionLayout {
      CompositionLocalProvider(
        LocalSharedTransitionScopes provides SharedTransitionScopes(
          shared = this,
          animated = null
        )
      ) {
        NavHost(
          navController = navController,
          startDestination = Routes.Home,
        ) {
          viceComposable<Routes.Home>(
            enterTransition = { slideInHorizontally(tween(500)) { it * 2 } },
            popEnterTransition = { slideInHorizontally(tween(500)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(500)) { it * 2 } },
            exitTransition = { slideOutHorizontally(tween(500)) { - it } },
          ) {
            HomeDestination(
              onNavigateToCreateItem = { navController.navigate(Routes.Details.Create) },
              onNavigateToUpdateItem = { id -> navController.navigate(Routes.Details.Update(id)) },
              onNavigateToSettings = { navController.navigate(Routes.Settings) },
              scope = this
            )
          }

          viceDialog<Routes.Details.Create> { op ->
            DetailsDestination(
              op = op,
              onNavigateBack = { navController.popBackStack() },
            )
          }

          viceDialog<Routes.Details.Update> { op ->
            DetailsDestination(
              op = op,
              onNavigateBack = { navController.popBackStack() },
            )
          }

          navigation<Routes.Settings>(
            startDestination = Routes.Settings.Home,
            enterTransition = { slideInHorizontally(tween(500)) { it * 2 } },
            popEnterTransition = { slideInHorizontally(tween(500)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(500)) { it * 2 } },
            exitTransition = { slideOutHorizontally(tween(500)) { -it } },
          ) {
            viceComposable<Routes.Settings.Home> {
              SettingsDestination(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAboutUs = {
                  navController.navigate(Routes.Settings.AboutUs)
                },
                scope = this
              )
            }

            viceComposable<Routes.Settings.AboutUs>(
            ) {
              AboutUsDestination(
                onNavigateBack = { navController.popBackStack() },
                scope = this,
              )
            }
          }
        }
      }
    }
  }
}
