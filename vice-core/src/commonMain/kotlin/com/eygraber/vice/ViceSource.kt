package com.eygraber.vice

import androidx.compose.runtime.Composable

public interface ViceSource<T> {
  @Composable
  public fun currentState(): T
}

public fun <T> ViceSource(state: @Composable () -> T): ViceSource<T> = object : ViceSource<T> {
  @Composable
  override fun currentState() = state()
}
