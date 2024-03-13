package com.eygraber.vice

@JsFun("() => { return Date.now(); }")
private external fun now(): Double

internal actual fun epochMillis(): Long = now().toLong()
