package com.eygraber.vice

import androidx.compose.runtime.Composable

public typealias ViceView<Intent, State> = @Composable (State, (Intent) -> Unit) -> Unit
