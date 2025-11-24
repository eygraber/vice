package com.eygraber.vice.sources

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MutableStateSourceTest {
  @Test
  fun `test that initial value is the correct value`() {
    assert(Source().value == 1)
  }

  @Test
  fun `test that updates work correctly`() = runTest {
    val source = Source(initial = 0)

    source.updates.test {
      repeat(5) { index ->
        assertEquals(index, awaitItem())

        Snapshot.withMutableSnapshot {
          source.increment()
        }
      }

      assertEquals(5, awaitItem())
    }
  }

  @Test
  fun `test that mutable state source works`() = runComposeUiTest {
    val source = Source()
    val values = mutableSetOf<Int>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(1), values)
    source.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2), values)
  }

  @Test
  fun `test that mutable state source is reset across recreation`() = runComposeUiTest {
    var source = Source()
    val values = mutableListOf<Int>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = Source() },
    )
    val disposedValues = mutableListOf<Boolean>()

    LocalSaveableStateRegistry.providesDefault(
      SaveableStateRegistry(
        restoredValues = null,
        canBeSaved = { true },
      ),
    )

    restorationTester.setContent {
      values += source.currentState()

      DisposableEffect(Unit) {
        disposedValues += false

        onDispose {
          disposedValues += true
        }
      }
    }

    assertContentEquals(listOf(1), values)
    assertContentEquals(listOf(false), disposedValues)
    source.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1, 2, 1), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2, 1, 2), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  private class Source(
    override val initial: Int = 1,
  ) : MutableStateSource<Int>() {
    fun increment() {
      update(value + 1)
    }
  }
}
