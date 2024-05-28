package com.eygraber.vice.nav

import androidx.navigation.NavBackStackEntry

public data class TypedNavBackStackEntry<T : Any>(
  public val route: T,
  public val entry: NavBackStackEntry,
)
