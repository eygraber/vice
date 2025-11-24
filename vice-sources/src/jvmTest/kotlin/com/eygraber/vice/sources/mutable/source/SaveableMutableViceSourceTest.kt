package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.saveableMutableViceSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SaveableMutableViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = saveableMutableViceSource(1)
    var value = 0
    setContent {
      value = source.currentState()
    }
    assertEquals(1, value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = saveableMutableViceSource(0)
    val values = mutableListOf<Int>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(0), values)

    source.update(1)
    awaitIdle()

    assertContentEquals(listOf(0, 1), values)
  }

  @Test
  fun `test that saveable mutable vice source is retained across recreation`() = runComposeUiTest {
    var source = saveableMutableViceSource(1)
    val values = mutableListOf<Int>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = saveableMutableViceSource(1) },
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
    source.update(2)
    awaitIdle()
    assertContentEquals(listOf(1, 2), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1, 2, 2), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(3)
    awaitIdle()
    assertContentEquals(listOf(1, 2, 2, 3), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that complex initial value is the correct value`() = runComposeUiTest {
    val source = saveableMutableViceSource(
      initial = ComplexData(1),
      saver = complexDataSaver,
    )

    var value = ComplexData(0)
    setContent {
      value = source.currentState()
    }
    assertEquals(1, value.data)
  }

  @Test
  fun `test that complex updates work correctly`() = runComposeUiTest {
    val source = saveableMutableViceSource(
      initial = ComplexData(0),
      saver = complexDataSaver,
    )
    val values = mutableListOf<ComplexData>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(ComplexData(0)), values)

    source.update(ComplexData(1))
    awaitIdle()

    assertContentEquals(listOf(ComplexData(0), ComplexData(1)), values)
  }

  @Test
  fun `test that complex saveable mutable vice source is retained across recreation`() = runComposeUiTest {
    var source = saveableMutableViceSource(
      initial = ComplexData(1),
      saver = complexDataSaver,
    )
    val values = mutableListOf<ComplexData>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = {
        source = saveableMutableViceSource(
          initial = ComplexData(1),
          saver = complexDataSaver,
        )
      },
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

    assertContentEquals(listOf(ComplexData(1)), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(ComplexData(2))
    awaitIdle()
    assertContentEquals(listOf(ComplexData(1), ComplexData(2)), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(ComplexData(1), ComplexData(2), ComplexData(2)), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(ComplexData(3))
    awaitIdle()
    assertContentEquals(listOf(ComplexData(1), ComplexData(2), ComplexData(2), ComplexData(3)), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  private data class ComplexData(val data: Int)

  private val complexDataSaver = Saver<ComplexData, Int>(
    save = {
      it.data
    },
    restore = {
      ComplexData(it)
    },
  )
}
