package com.eygraber.vice

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual fun epochMillis(): Long = (NSDate().timeIntervalSince1970 * 1_000).toLong()
