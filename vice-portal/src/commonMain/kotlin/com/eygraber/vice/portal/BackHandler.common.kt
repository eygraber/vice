package com.eygraber.vice.portal

import androidx.compose.runtime.Composable

@Composable
public expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)
