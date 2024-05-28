package com.eygraber.vice.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlin.reflect.KType

public actual inline fun <reified T : Any> NavGraphBuilder.viceComposable(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
  deepLinks: List<NavDeepLink>,
  noinline enterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
  noinline exitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
  noinline popEnterTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
  noinline popExitTransition:
  (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
  noinline sizeTransform: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    @JvmSuppressWildcards SizeTransform?
  )?,
  crossinline destinationFactory: (TypedNavBackStackEntry<T>) -> ViceDestination<*, *, *, *>,
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
    remember(it) { destinationFactory(TypedNavBackStackEntry(it.toRoute<T>(), it)) }.Vice()
  }
}

public actual inline fun <reified T : Any> NavGraphBuilder.viceDialog(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
  deepLinks: List<NavDeepLink>,
  dialogProperties: DialogProperties,
  crossinline destinationFactory: (TypedNavBackStackEntry<T>) -> ViceDestination<*, *, *, *>,
) {
  dialog<T>(
    typeMap,
    deepLinks,
    dialogProperties,
  ) {
    remember(it) { destinationFactory(TypedNavBackStackEntry(it.toRoute<T>(), it)) }.Vice()
  }
}
