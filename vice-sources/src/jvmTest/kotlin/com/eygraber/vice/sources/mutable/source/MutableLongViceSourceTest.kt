package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.mutableLongSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MutableLongViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = mutableLongSource(1L)
    var value = 0L
    setContent {
      value = source.currentState()
    }
    assertEquals(1L, value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = mutableLongSource(0L)
    val values = mutableListOf<Long>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(0L), values)

    source.update(1L)
    awaitIdle()

    assertContentEquals(listOf(0L, 1L), values)
  }

  @Test
  fun `test that mutable long source is reset across recreation`() = runComposeUiTest {
    var source = mutableLongSource(1L)
    val values = mutableListOf<Long>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableLongSource(1L) },
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

    assertContentEquals(listOf(1L), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2L)
    awaitIdle()
    assertContentEquals(listOf(1L, 2L), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1L, 2L, 1L), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(2L)
    awaitIdle()
    assertContentEquals(listOf(1L, 2L, 1L, 2L), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that saveable mutable long source is retained across recreation`() = runComposeUiTest {
    var source = mutableLongSource(1L, isSaveable = true)
    val values = mutableListOf<Long>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableLongSource(1L, isSaveable = true) },
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

    assertContentEquals(listOf(1L), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2L)
    awaitIdle()
    assertContentEquals(listOf(1L, 2L), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1L, 2L, 2L), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(3L)
    awaitIdle()
    assertContentEquals(listOf(1L, 2L, 2L, 3L), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }
}
