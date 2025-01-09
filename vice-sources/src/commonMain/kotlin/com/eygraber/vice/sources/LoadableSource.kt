package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.eygraber.vice.loadable.ViceLoadable
import com.eygraber.vice.loadable.isLoading
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

public abstract class LoadableSource<T> : StateSource<ViceLoadable<T>> {
  protected open val initialLoadingThreshold: Duration = 200.milliseconds
  protected open val minLoadingDuration: Duration = 1.seconds

  public abstract val placeholder: T

  private val loadingPlaceholder get() = ViceLoadable.Loading(placeholder)

  final override var value: ViceLoadable<T> = loadingPlaceholder
    private set

  protected abstract suspend fun load(): T

  @Composable
  override fun currentState(): ViceLoadable<T> {
    LaunchedEffect(Unit) {
      val initialLoadingThreshold = TimeSource.Monotonic.markNow() + initialLoadingThreshold
      val minLoadingThreshold = initialLoadingThreshold + minLoadingDuration

      val loadedValue = load()
      if(value.isLoading) {
        val now = TimeSource.Monotonic.markNow()
        if(now in initialLoadingThreshold..<minLoadingThreshold) {
          delay(minLoadingThreshold - now)
        }
      }

      value = ViceLoadable.Loaded(loadedValue)
    }

    return super.currentState()
  }
}
