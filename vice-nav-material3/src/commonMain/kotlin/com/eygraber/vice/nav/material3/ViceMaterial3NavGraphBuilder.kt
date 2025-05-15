package com.eygraber.vice.nav.material3

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.toRoute
import com.eygraber.compose.material3.navigation.ModalBottomSheetNavigator
import com.eygraber.compose.material3.navigation.bottomSheet
import com.eygraber.compose.material3.navigation.defaultWindowInsets
import com.eygraber.vice.nav.TypedNavBackStackEntry
import com.eygraber.vice.nav.ViceDestination
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

@ExperimentalMaterial3Api
public fun NavGraphBuilder.viceBottomSheet(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  modalBottomSheetModifier: Modifier = Modifier,
  modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
  skipPartiallyExpanded: Boolean = false,
  confirmValueChange: (SheetValue) -> Boolean = { true },
  contentWindowInsets: @Composable (SheetState) -> WindowInsets = ModalBottomSheetNavigator.defaultWindowInsets(),
  dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
  destinationFactory: (NavBackStackEntry) -> ViceDestination<*, *, *, *>,
) {
  bottomSheet(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    modalBottomSheetModifier = modalBottomSheetModifier,
    modalBottomSheetProperties = modalBottomSheetProperties,
    skipPartiallyExpanded = skipPartiallyExpanded,
    confirmValueChange = confirmValueChange,
    contentWindowInsets = contentWindowInsets,
    dragHandle = dragHandle,
  ) {
    remember(it.id) { destinationFactory(it) }.Vice()
  }
}

@ExperimentalMaterial3Api
public inline fun <reified T : Any> NavGraphBuilder.viceBottomSheet(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  modalBottomSheetModifier: Modifier = Modifier,
  modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
  skipPartiallyExpanded: Boolean = false,
  noinline confirmValueChange: (SheetValue) -> Boolean = { true },
  noinline contentWindowInsets: @Composable (SheetState) -> WindowInsets =
    ModalBottomSheetNavigator.defaultWindowInsets(),
  noinline dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
  crossinline destinationFactory: (TypedNavBackStackEntry<T>) -> ViceDestination<*, *, *, *>,
) {
  bottomSheet<T>(
    typeMap = typeMap,
    deepLinks = deepLinks,
    modalBottomSheetModifier = modalBottomSheetModifier,
    modalBottomSheetProperties = modalBottomSheetProperties,
    skipPartiallyExpanded = skipPartiallyExpanded,
    confirmValueChange = confirmValueChange,
    contentWindowInsets = contentWindowInsets,
    dragHandle = dragHandle,
  ) {
    remember(it.id) { destinationFactory(TypedNavBackStackEntry(it.toRoute<T>(), it)) }.Vice()
  }
}
