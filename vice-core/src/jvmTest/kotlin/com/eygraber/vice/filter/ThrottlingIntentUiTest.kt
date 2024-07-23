package com.eygraber.vice.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceContainer
import com.eygraber.vice.ViceEffects
import com.eygraber.vice.ViceView
import com.eygraber.vice.filter.ThrottlingIntentUiTest.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource

@OptIn(ExperimentalTestApi::class)
class ThrottlingIntentUiTest {
  sealed interface Intent {
    data object Regular : Intent
    data object DefaultThrottlingIntent : ThrottlingIntent, Intent
    data object DefaultThrottlingIntent2 : ThrottlingIntent, Intent
    data class CustomKeyThrottlingIntent(private val i: Int) : ThrottlingIntent, Intent {
      override fun key() = i
    }
  }

  @Test
  fun clickingAButton_thatEmitsANonThrottlingIntent_isNotThrottled() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.Regular }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()

        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.Regular),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.Regular, Intent.Regular),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsAThrottlingIntent_doesNotThrottleTheFirstIntent() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_usingTheDefaultKey_throttles() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_usingTheDefaultKey_throttles_butDoesNotThrottleAThrottlingIntentWithADifferentKey() =
    runComposeUiTest {
      val container = object : TestContainer() {
        private var i = 0
        override val intentProvider: () -> Intent = {
          when(i++) {
            0, 1 -> Intent.DefaultThrottlingIntent
            else -> Intent.DefaultThrottlingIntent2
          }
        }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent2),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_usingTheDefaultKey_throttles_andAlsoThrottlesMultipleThrottlingIntentsWithADifferentKey() =
    runComposeUiTest {
      val container = object : TestContainer() {
        private var i = 0
        override val intentProvider: () -> Intent = {
          when(i++) {
            0, 1 -> Intent.DefaultThrottlingIntent
            else -> Intent.DefaultThrottlingIntent2
          }
        }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent2),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent2),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButtonMultipleTimes_thatEmitsThrottlingIntentsWithTheSameKey_usingTheDefaultKey_throttles() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_usingACustomKey_throttles() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.CustomKeyThrottlingIntent(1) }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButtonMultipleTimes_thatEmitsThrottlingIntentsWithTheSameKey_usingACustomKey_throttles() =
    runComposeUiTest {
      val container = object : TestContainer() {
        override val intentProvider = { Intent.CustomKeyThrottlingIntent(1) }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithDifferentKeys_usingTheDefaultKey_doesNotThrottle() =
    runComposeUiTest {
      val container = object : TestContainer() {
        private var i = 0
        override val intentProvider: () -> Intent = {
          when(i++) {
            0 -> Intent.DefaultThrottlingIntent
            else -> Intent.DefaultThrottlingIntent2
          }
        }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent2),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithDifferentKeys_usingACustomKey_doesNotThrottle() =
    runComposeUiTest {
      val container = object : TestContainer() {
        private var i = 0

        override val intentProvider = { Intent.CustomKeyThrottlingIntent(i++) }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(0)),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.CustomKeyThrottlingIntent(0), Intent.CustomKeyThrottlingIntent(1)),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_doesNotThrottleAfterTheIntervalHasPassed() =
    runComposeUiTest {
      val testTimeSource = TestTimeSource()
      val filter = ThrottlingIntentFilter(
        timeSource = testTimeSource,
      )
      val container = object : TestContainer(filter) {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += Intent.DefaultThrottlingIntent.interval
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += 1.milliseconds
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_throttlesAfterTheIntervalHasPassed_andAnotherThrottlingIntentIsEmitted() =
    runComposeUiTest {
      val testTimeSource = TestTimeSource()
      val filter = ThrottlingIntentFilter(
        timeSource = testTimeSource,
      )
      val container = object : TestContainer(filter) {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += Intent.DefaultThrottlingIntent.interval
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += 1.milliseconds
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent),
          container = container,
        )
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent),
          container = container,
        )
      }
    }

  @Test
  fun clickingAButton_thatEmitsThrottlingIntentsWithTheSameKey_doesNotThrottleAfterASecondThrottlingIntentIsEmitted_andTheIntervalHasPassedAgain() =
    runComposeUiTest {
      val testTimeSource = TestTimeSource()
      val filter = ThrottlingIntentFilter(
        timeSource = testTimeSource,
      )
      val container = object : TestContainer(filter) {
        override val intentProvider = { Intent.DefaultThrottlingIntent }
      }

      setContent {
        container.Vice()
      }

      with(onNodeWithTag("subject")) {
        assertExists()
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += Intent.DefaultThrottlingIntent.interval
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += 1.milliseconds
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += Intent.DefaultThrottlingIntent.interval
        performClickAndAssertIntentsEquals(
          expected = listOf(Intent.DefaultThrottlingIntent, Intent.DefaultThrottlingIntent),
          container = container,
        )
        testTimeSource += 1.milliseconds
        performClickAndAssertIntentsEquals(
          expected = listOf(
            Intent.DefaultThrottlingIntent,
            Intent.DefaultThrottlingIntent,
            Intent.DefaultThrottlingIntent,
          ),
          container = container,
        )
      }
    }

  private fun SemanticsNodeInteraction.performClickAndAssertIntentsEquals(
    expected: List<Intent>,
    container: TestContainer,
  ) {
    runBlocking(Dispatchers.Main.immediate) {
      container.lock.lock()

      performClick()

      container.lock.withLock {
        assertContentEquals(
          expected = expected,
          actual = container.intents,
          message = "Expected $expected; actual ${container.intents}",
        )
      }
    }
  }
}

private abstract class TestContainer(
  vararg intentFilters: ViceIntentFilter = arrayOf(ThrottlingIntentFilter()),
) : ViceContainer<Intent, ViceCompositor<Intent, Any>, ViceEffects, Any>(*intentFilters) {
  abstract val intentProvider: () -> Intent

  override val view: ViceView<Intent, Any> = @Composable { _, onIntent ->
    Box(
      modifier = Modifier
        .size(20.dp)
        .testTag("subject")
        .clickable {
          onIntent(intentProvider())
          lock.unlock()
        },
    )
  }

  val lock = Mutex()

  val intents = ArrayList<Intent>()

  override val compositor = object : ViceCompositor<Intent, Any> {
    @Composable
    override fun composite() = Any()

    override suspend fun onIntent(intent: Intent) {
      intents += intent
    }
  }
  override val effects = ViceEffects.None
}
