package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.mutableFloatSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MutableFloatViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = mutableFloatSource(1F)
    var value = 0F
    setContent {
      value = source.currentState()
    }
    assertEquals(1F, value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = mutableFloatSource(0F)
    val values = mutableListOf<Float>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(0F), values)

    source.update(1F)
    awaitIdle()

    assertContentEquals(listOf(0F, 1F), values)
  }

  @Test
  fun `test that mutable float source is reset across recreation`() = runComposeUiTest {
    var source = mutableFloatSource(1F)
    val values = mutableListOf<Float>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableFloatSource(1F) },
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

    assertContentEquals(listOf(1F), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2F)
    awaitIdle()
    assertContentEquals(listOf(1F, 2F), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1F, 2F, 1F), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(2F)
    awaitIdle()
    assertContentEquals(listOf(1F, 2F, 1F, 2F), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that saveable mutable float source is retained across recreation`() = runComposeUiTest {
    var source = mutableFloatSource(1F, isSaveable = true)
    val values = mutableListOf<Float>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableFloatSource(1F, isSaveable = true) },
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

    assertContentEquals(listOf(1F), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2F)
    awaitIdle()
    assertContentEquals(listOf(1F, 2F), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1F, 2F, 2F), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(3F)
    awaitIdle()
    assertContentEquals(listOf(1F, 2F, 2F, 3F), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }
}
