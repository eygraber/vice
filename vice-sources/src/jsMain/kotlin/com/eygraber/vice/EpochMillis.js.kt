package com.eygraber.vice

import kotlin.js.Date

internal actual fun epochMillis(): Long = Date.now().toLong()
