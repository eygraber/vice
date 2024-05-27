package com.eygraber.vice.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
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
  noinline sizeTransform: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards SizeTransform?
  )? = null,
  crossinline destinationFactory: (NavBackStackEntry) -> ViceDestination<*, *, *, *>,
) {
  composable(
    route,
    arguments,
    deepLinks,
    enterTransition,
    exitTransition,
    popEnterTransition,
    popExitTransition,
    sizeTransform,
  ) {
    remember(it) { destinationFactory(it) }.Vice()
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
  crossinline destinationFactory: (T) -> ViceDestination<*, *, *, *>,
) {
  composable<T>(
    typeMap,
    deepLinks,
    enterTransition,
    exitTransition,
    popEnterTransition,
    popExitTransition,
    sizeTransform,
  ) {
    remember(it) { destinationFactory(it.toRoute<T>()) }.Vice()
  }
}

public inline fun <reified T : Any> NavGraphBuilder.viceDialog(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  dialogProperties: DialogProperties = DialogProperties(),
  crossinline destinationFactory: (T) -> ViceDestination<*, *, *, *>,
) {
  dialog<T>(
    typeMap,
    deepLinks,
    dialogProperties,
  ) {
    remember(it) { destinationFactory(it.toRoute<T>()) }.Vice()
  }
}