package com.eygraber.vice

import platform.Foundation.NSDate

internal actual fun epochMillis(): Long = (NSDate().timeIntervalSince1970 * 1_000).toLong()
