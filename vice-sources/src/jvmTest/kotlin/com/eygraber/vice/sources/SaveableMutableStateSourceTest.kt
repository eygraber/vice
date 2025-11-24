package com.eygraber.vice.sources

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SaveableMutableStateSourceTest {
  @Test
  fun `test that initial value is the correct value`() {
    assert(SimpleSource().value == 1)
  }

  @Test
  fun `test that updates work correctly for simple sources`() = runTest {
    val source = SimpleSource(initial = 0)

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
  fun `test that updates work correctly for complex sources`() = runTest {
    val source = ComplexSource(initial = ComplexData(0))

    source.updates.test {
      repeat(5) { index ->
        assertEquals(index, awaitItem().data)

        Snapshot.withMutableSnapshot {
          source.increment()
        }
      }

      assertEquals(5, awaitItem().data)
    }
  }

  @Test
  fun `test that simple saveable mutable state source works`() = runComposeUiTest {
    val simpleSource = SimpleSource()
    val values = mutableSetOf<Int>()
    val disposedValues = mutableListOf<Boolean>()

    setContent {
      values += simpleSource.currentState()

      DisposableEffect(Unit) {
        disposedValues += false

        onDispose {
          disposedValues += true
        }
      }
    }

    assertContentEquals(listOf(1), values)
    assertContentEquals(listOf(false), disposedValues)
    simpleSource.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2), values)
    assertContentEquals(listOf(false), disposedValues)
  }

  @Test
  fun `test that simple saveable mutable state source works across recreation`() = runComposeUiTest {
    var simpleSource = SimpleSource()
    val values = mutableSetOf<Int>()
    val disposedValues = mutableListOf<Boolean>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { simpleSource = SimpleSource() },
    )

    LocalSaveableStateRegistry.providesDefault(
      SaveableStateRegistry(
        restoredValues = null,
        canBeSaved = { true },
      ),
    )

    restorationTester.setContent {
      values += simpleSource.currentState()

      DisposableEffect(Unit) {
        disposedValues += false

        onDispose {
          disposedValues += true
        }
      }
    }

    assertContentEquals(listOf(1), values)
    assertContentEquals(listOf(false), disposedValues)
    simpleSource.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1, 2), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    simpleSource.increment()
    awaitIdle()
    assertContentEquals(listOf(1, 2, 3), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that complex saveable mutable state source works`() = runComposeUiTest {
    val complexSource = ComplexSource()
    val values = mutableSetOf<ComplexData>()
    val disposedValues = mutableListOf<Boolean>()

    setContent {
      values += complexSource.currentState()

      DisposableEffect(Unit) {
        disposedValues += false

        onDispose {
          disposedValues += true
        }
      }
    }

    assertContentEquals(listOf(ComplexData(1)), values)
    assertContentEquals(listOf(false), disposedValues)
    complexSource.increment()
    awaitIdle()
    assertContentEquals(listOf(ComplexData(1), ComplexData(2)), values)
    assertContentEquals(listOf(false), disposedValues)
  }

  @Test
  fun `test that complex saveable mutable state source works across recreation`() = runComposeUiTest {
    var complexSource = ComplexSource()
    val values = mutableSetOf<ComplexData>()
    val disposedValues = mutableListOf<Boolean>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { complexSource = ComplexSource() },
    )

    LocalSaveableStateRegistry.providesDefault(
      SaveableStateRegistry(
        restoredValues = null,
        canBeSaved = { true },
      ),
    )

    restorationTester.setContent {
      values += complexSource.currentState()

      DisposableEffect(Unit) {
        disposedValues += false

        onDispose {
          disposedValues += true
        }
      }
    }

    assertContentEquals(listOf(ComplexData(1)), values)
    assertContentEquals(listOf(false), disposedValues)
    complexSource.increment()
    awaitIdle()
    assertContentEquals(listOf(ComplexData(1), ComplexData(2)), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(ComplexData(1), ComplexData(2)), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    complexSource.increment()
    awaitIdle()
    assertContentEquals(listOf(ComplexData(1), ComplexData(2), ComplexData(3)), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  private class SimpleSource(
    override val initial: Int = 1,
  ) : SaveableMutableStateSource<Int>() {
    fun increment() {
      update(value + 1)
    }
  }

  private data class ComplexData(val data: Int)

  private class ComplexSource(
    override val initial: ComplexData = ComplexData(1),
  ) : SaveableMutableStateSource<ComplexData>(
    saver = Saver(
      save = {
        it.data
      },
      restore = {
        ComplexData(it)
      },
    ),
  ) {
    fun increment() {
      update(ComplexData(value.data + 1))
    }
  }
}
