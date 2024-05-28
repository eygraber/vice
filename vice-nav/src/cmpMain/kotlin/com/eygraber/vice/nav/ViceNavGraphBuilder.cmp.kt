package com.eygraber.vice.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import kotlin.jvm.JvmSuppressWildcards
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
  crossinline destinationFactory: (T) -> ViceDestination<*, *, *, *>,
) {
  @Suppress("NotImplementedDeclaration")
  TODO("Not implemented yet; waiting for CMP navigation to support type safe APIs")
}

public actual inline fun <reified T : Any> NavGraphBuilder.viceDialog(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
  deepLinks: List<NavDeepLink>,
  dialogProperties: DialogProperties,
  crossinline destinationFactory: (T) -> ViceDestination<*, *, *, *>,
) {
  @Suppress("NotImplementedDeclaration")
  TODO("Not implemented yet; waiting for CMP navigation to support type safe APIs")
}
