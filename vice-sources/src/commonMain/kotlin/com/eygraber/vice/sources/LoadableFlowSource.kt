package com.eygraber.vice.sources

import androidx.compose.runtime.Stable
import com.eygraber.vice.loadable.ViceLoadable
import com.eygraber.vice.loadable.isLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@Stable
public abstract class LoadableFlowSource<T>(
  initializationThreadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) : StateFlowSource<ViceLoadable<T>>() {
  private val mutableFlow by lazy(initializationThreadSafetyMode) {
    MutableStateFlow<ViceLoadable<T>>(ViceLoadable.Loading(placeholder))
  }

  override val flow: StateFlow<ViceLoadable<T>> by lazy(initializationThreadSafetyMode) { mutableFlow }

  protected abstract val dataFlow: Flow<T>

  protected abstract val placeholder: T

  protected open val initialLoadingThreshold: Duration = 200.milliseconds
  protected open val minLoadingDuration: Duration = 1.seconds

  protected open val isBufferingEmissionsWhileLoading: Boolean = false

  override suspend fun onAttached(scope: CoroutineScope) {
    val initialLoadingThreshold = TimeSource.Monotonic.markNow() + initialLoadingThreshold
    val minLoadingThreshold = initialLoadingThreshold + minLoadingDuration

    val collector: suspend (T) -> Unit = { value ->
      if(flow.value.isLoading) {
        val now = TimeSource.Monotonic.markNow()
        if(now in initialLoadingThreshold..<minLoadingThreshold) {
          delay(minLoadingThreshold - now)
        }
      }

      mutableFlow.value = ViceLoadable.Loaded(value)
    }

    when {
      isBufferingEmissionsWhileLoading -> dataFlow.collect(collector)
      else -> dataFlow.collectLatest(collector)
    }
  }
}
