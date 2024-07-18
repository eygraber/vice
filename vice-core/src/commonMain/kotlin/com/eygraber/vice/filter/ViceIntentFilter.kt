package com.eygraber.vice.filter

/**
 * An interface representing a filter for Intents.
 * This can be used to selectively process or discard Intents based on custom logic.
 * Implementations of this interface can apply different filtering strategies.
 *
 * Examples of use cases:
 * - Throttling Intents to prevent excessive processing of frequent user actions.
 * - Filtering out certain types of Intents based on application state or user permissions.
 */
public interface ViceIntentFilter {
  /**
   * Filters the given Intent.
   *
   * @param intent the Intent to filter
   * @return true if the Intent passes the filter, false otherwise
   */
  public fun filter(intent: Any): Boolean
}
