package com.eygraber.vice.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

public inline fun NavGraphBuilder.viceComposable(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  noinline enterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
  noinline exitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
  noinline popEnterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
    enterTransition,
  noinline popExitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
    exitTransition,
  crossinline destinationFactory: (NavBackStackEntry) -> ViceDestination<*, *, *, *>,
) {
  composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
  ) { entry ->
    CompositionLocalProvider(
      LocalAnimatedVisibilityScope provides this,
    ) {
      remember(entry.id) { destinationFactory(entry) }.Vice()
    }
  }
}

public inline fun <reified T : Any> NavGraphBuilder.viceComposable(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  noinline enterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
  noinline exitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
  noinline popEnterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
    enterTransition,
  noinline popExitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
    exitTransition,
  noinline sizeTransform: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards SizeTransform?
  )? = null,
  crossinline destinationFactory: (TypedNavBackStackEntry<T>) -> ViceDestination<*, *, *, *>,
) {
  composable<T>(
    typeMap = typeMap,
    deepLinks = deepLinks,
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
    sizeTransform = sizeTransform,
  ) { entry ->
    CompositionLocalProvider(
      LocalAnimatedVisibilityScope provides this,
    ) {
      remember(entry.id) { destinationFactory(TypedNavBackStackEntry(entry.toRoute<T>(), entry)) }.Vice()
    }
  }
}

public fun NavGraphBuilder.viceDialog(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  dialogProperties: DialogProperties = DialogProperties(),
  destinationFactory: (NavBackStackEntry) -> ViceDestination<*, *, *, *>,
) {
  dialog(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
  ) {
    remember(it.id) { destinationFactory(it) }.Vice()
  }
}

public inline fun <reified T : Any> NavGraphBuilder.viceDialog(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  dialogProperties: DialogProperties = DialogProperties(),
  crossinline destinationFactory: (TypedNavBackStackEntry<T>) -> ViceDestination<*, *, *, *>,
) {
  dialog<T>(
    typeMap = typeMap,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
  ) {
    remember(it.id) { destinationFactory(TypedNavBackStackEntry(it.toRoute<T>(), it)) }.Vice()
  }
}
