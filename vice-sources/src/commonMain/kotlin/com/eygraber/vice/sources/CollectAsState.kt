package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
internal expect fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T>

@Composable
internal expect fun <T> Flow<T>.collectAsStateWithLifecycle(initial: T): State<T>
