package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.mutableIntSource
import com.eygraber.vice.sources.saveableMutableIntSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MutableIntViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = mutableIntSource(1)
    var value = 0
    setContent {
      value = source.currentState()
    }
    assertEquals(1, value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = mutableIntSource(0)
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
  fun `test that mutable int source is reset across recreation`() = runComposeUiTest {
    var source = mutableIntSource(1)
    val values = mutableListOf<Int>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableIntSource(1) },
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

    assertContentEquals(listOf(1, 2, 1), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(2)
    awaitIdle()
    assertContentEquals(listOf(1, 2, 1, 2), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that saveable mutable int source is retained across recreation`() = runComposeUiTest {
    var source = saveableMutableIntSource(1)
    val values = mutableListOf<Int>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = saveableMutableIntSource(1) },
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
}
