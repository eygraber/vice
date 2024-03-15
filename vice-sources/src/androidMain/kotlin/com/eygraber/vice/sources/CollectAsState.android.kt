package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle as androidCollectAsStateWithLifecycle

@Composable
internal actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> =
  androidCollectAsStateWithLifecycle()

@Composable
internal actual fun <T> Flow<T>.collectAsStateWithLifecycle(initial: T): State<T> =
  androidCollectAsStateWithLifecycle(initial)
