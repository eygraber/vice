package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import com.eygraber.vice.ViceSource

public interface StateSource<T> : ViceSource<T>, State<T> {
  @Composable
  @ReadOnlyComposable
  override fun currentState(): T = value
}
