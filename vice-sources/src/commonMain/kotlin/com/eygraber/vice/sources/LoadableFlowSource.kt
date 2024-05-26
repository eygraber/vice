package com.eygraber.vice.sources

import androidx.compose.runtime.Stable
import com.eygraber.vice.epochMillis
import com.eygraber.vice.loadable.ViceLoadable
import com.eygraber.vice.loadable.isLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
public abstract class LoadableFlowSource<T> : StateFlowSource<ViceLoadable<T>>() {
  private val mutableFlow by lazy {
    MutableStateFlow<ViceLoadable<T>>(ViceLoadable.Loading(placeholder))
  }

  override val flow: StateFlow<ViceLoadable<T>> by lazy { mutableFlow }

  protected abstract val dataFlow: Flow<T>

  protected abstract val placeholder: T

  protected open val initialLoadingThresholdMillis: Int = 200
  protected open val minLoadingDurationMillis: Long = 1_000L

  override suspend fun onAttached(scope: CoroutineScope) {
    val initialLoadingThreshold = epochMillis() + initialLoadingThresholdMillis
    val minLoadingThreshold = initialLoadingThreshold + minLoadingDurationMillis

    dataFlow.collect { value ->
      if(flow.value.isLoading) {
        val now = epochMillis()
        if(now in initialLoadingThreshold..<minLoadingThreshold) {
          delay(minLoadingThreshold - now)
        }
      }

      mutableFlow.value = ViceLoadable.Loaded(value)
    }
  }
}
