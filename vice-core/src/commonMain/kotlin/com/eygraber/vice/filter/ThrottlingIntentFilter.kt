package com.eygraber.vice.filter

import kotlin.time.ComparableTimeMark
import kotlin.time.TimeSource

/**
 * A filter that throttles Intents based on a specified interval.
 * It filters out Intents if there was a previous emission of an Intent
 * with a matching [ThrottlingIntent.key] that occurred within the throttling interval.
 *
 * @property timeSource the [TimeSource] used to track time marks.
 * Defaults to [TimeSource.Monotonic].
 */
public class ThrottlingIntentFilter(
  private val timeSource: TimeSource.WithComparableMarks = TimeSource.Monotonic,
) : ViceIntentFilter {
  private val lookup = HashMap<Any, ComparableTimeMark>()

  /**
   * Filters the given Intent based on its throttling interval.
   * If the Intent is an instance of [ThrottlingIntent], it will be throttled
   * based on the time of the last emitted intent with the same [ThrottlingIntent.key].
   *
   * @param intent the Intent to filter
   * @return true if the Intent passes the filter, false otherwise
   */
  override fun filter(intent: Any): Boolean = when(intent) {
    is ThrottlingIntent -> {
      val now = timeSource.markNow()
      val key = intent.key()

      val lastEmit = lookup[key]

      val isThrottled = lastEmit != null && now - lastEmit <= intent.interval

      if(!isThrottled) {
        lookup[key] = now
      }

      !isThrottled
    }

    else -> true
  }
}
