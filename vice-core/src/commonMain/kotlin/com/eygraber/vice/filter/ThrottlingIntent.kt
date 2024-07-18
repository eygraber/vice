package com.eygraber.vice.filter

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * An interface representing an intent that can be throttled.
 * Implementing classes can define their own throttling interval and key.
 */
public interface ThrottlingIntent {
  /**
   * The duration between Intent emissions that Intents with matching [key] will be throttled.
   * Defaults to 500 milliseconds.
   */
  public val interval: Duration get() = 500.milliseconds

  /**
   * The key used to identify the Intent. By default, uses the class of the Intent.
   *
   * @return the key identifying this Intent
   */
  public fun key(): Any = this::class
}
