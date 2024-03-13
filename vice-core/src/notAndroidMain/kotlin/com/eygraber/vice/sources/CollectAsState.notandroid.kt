package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
internal actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> = collectAsState()

@Composable
internal actual fun <T> Flow<T>.collectAsStateWithLifecycle(initial: T): State<T> = collectAsState(initial)
