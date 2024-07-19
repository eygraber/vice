package com.eygraber.vice.filter

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource

class ThrottlingIntentFilterTest {
  class DefaultThrottlingIntent : ThrottlingIntent
  class DefaultThrottlingIntent2 : ThrottlingIntent
  class CustomKeyThrottlingIntent(private val i: Int) : ThrottlingIntent {
    override fun key() = i
  }

  @Test
  fun filter_allowsNonThrottlingIntents() {
    assertTrue { ThrottlingIntentFilter().filter(Any()) }
  }

  @Test
  fun filter_doesNotThrottleFirstThrottlingIntent() {
    assertTrue { ThrottlingIntentFilter().filter(DefaultThrottlingIntent()) }
  }

  @Test
  fun filter_throttlesThrottlingIntentsWithTheSameKey_usingTheDefaultKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
  }

  @Test
  fun filter_throttlesThrottlingIntentsWithTheSameKey_usingTheDefaultKey_butDoesNotThrottleAThrottlingIntentWithADifferentKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
    assertTrue { filter.filter(DefaultThrottlingIntent2()) }
  }

  @Test
  fun filter_throttlesThrottlingIntentsWithTheSameKey_usingTheDefaultKey_andAlsoThrottlesMultipleThrottlingIntentsWithADifferentKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
    assertTrue { filter.filter(DefaultThrottlingIntent2()) }
    assertFalse { filter.filter(DefaultThrottlingIntent2()) }
  }

  @Test
  fun filter_throttlesMultipleThrottlingIntentsWithTheSameKey_usingTheDefaultKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
    assertFalse { filter.filter(DefaultThrottlingIntent()) }
  }

  @Test
  fun filter_throttlesThrottlingIntentsWithTheSameKey_usingACustomKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(CustomKeyThrottlingIntent(1)) }
    assertFalse { filter.filter(CustomKeyThrottlingIntent(1)) }
  }

  @Test
  fun filter_throttlesMultipleThrottlingIntentsWithTheSameKey_usingACustomKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(CustomKeyThrottlingIntent(1)) }
    assertFalse { filter.filter(CustomKeyThrottlingIntent(1)) }
    assertFalse { filter.filter(CustomKeyThrottlingIntent(1)) }
    assertFalse { filter.filter(CustomKeyThrottlingIntent(1)) }
  }

  @Test
  fun filter_doesNotThrottleThrottlingIntentsWithDifferentKeys_usingTheDefaultKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(DefaultThrottlingIntent()) }
    assertTrue { filter.filter(DefaultThrottlingIntent2()) }
  }

  @Test
  fun filter_doesNotThrottleThrottlingIntentsWithDifferentKeys_usingACustomKey() {
    val filter = ThrottlingIntentFilter()
    assertTrue { filter.filter(CustomKeyThrottlingIntent(1)) }
    assertTrue { filter.filter(CustomKeyThrottlingIntent(2)) }
  }

  @Test
  fun filter_doesNotThrottleThrottlingIntentsWithTheSameKey_afterTheIntervalHasPassed() {
    val testTimeSource = TestTimeSource()
    val filter = ThrottlingIntentFilter(
      timeSource = testTimeSource,
    )

    val intent = DefaultThrottlingIntent()
    assertTrue { filter.filter(intent) }
    testTimeSource += intent.interval
    assertFalse { filter.filter(intent) }
    testTimeSource += 1.milliseconds
    assertTrue { filter.filter(intent) }
  }

  @Test
  fun filter_throttlesThrottlingIntentsWithTheSameKey_afterTheIntervalHasPassed_andAnotherThrottlingIntentIsEmitted() {
    val testTimeSource = TestTimeSource()
    val filter = ThrottlingIntentFilter(
      timeSource = testTimeSource,
    )

    val intent = DefaultThrottlingIntent()
    assertTrue { filter.filter(intent) }
    testTimeSource += intent.interval
    assertFalse { filter.filter(intent) }
    testTimeSource += 1.milliseconds
    assertTrue { filter.filter(intent) }
    assertFalse { filter.filter(intent) }
  }

  @Test
  fun filter_doesNotThrottleASecondThrottlingIntentWithTheSameKey_afterTheIntervalHasPassedAgain() {
    val testTimeSource = TestTimeSource()
    val filter = ThrottlingIntentFilter(
      timeSource = testTimeSource,
    )

    val intent = DefaultThrottlingIntent()
    assertTrue { filter.filter(intent) }
    testTimeSource += intent.interval
    assertFalse { filter.filter(intent) }
    testTimeSource += 1.milliseconds
    assertTrue { filter.filter(intent) }
    testTimeSource += intent.interval
    assertFalse { filter.filter(intent) }
    testTimeSource += 1.milliseconds
    assertTrue { filter.filter(intent) }
  }
}
