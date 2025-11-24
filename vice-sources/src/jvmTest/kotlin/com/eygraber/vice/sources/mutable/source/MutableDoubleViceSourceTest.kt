package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.mutableDoubleSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MutableDoubleViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = mutableDoubleSource(1.0)
    var value = 0.0
    setContent {
      value = source.currentState()
    }
    assertEquals(1.0, value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = mutableDoubleSource(0.0)
    val values = mutableListOf<Double>()

    setContent {
      values += source.currentState()
    }

    assertContentEquals(listOf(0.0), values)

    source.update(1.0)
    awaitIdle()

    assertContentEquals(listOf(0.0, 1.0), values)
  }

  @Test
  fun `test that mutable double source is reset across recreation`() = runComposeUiTest {
    var source = mutableDoubleSource(1.0)
    val values = mutableListOf<Double>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableDoubleSource(1.0) },
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

    assertContentEquals(listOf(1.0), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2.0)
    awaitIdle()
    assertContentEquals(listOf(1.0, 2.0), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1.0, 2.0, 1.0), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(2.0)
    awaitIdle()
    assertContentEquals(listOf(1.0, 2.0, 1.0, 2.0), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }

  @Test
  fun `test that saveable mutable double source is retained across recreation`() = runComposeUiTest {
    var source = mutableDoubleSource(1.0, isSaveable = true)
    val values = mutableListOf<Double>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = { source = mutableDoubleSource(1.0, isSaveable = true) },
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

    assertContentEquals(listOf(1.0), values)
    assertContentEquals(listOf(false), disposedValues)
    source.update(2.0)
    awaitIdle()
    assertContentEquals(listOf(1.0, 2.0), values)
    assertContentEquals(listOf(false), disposedValues)

    restorationTester.emulateSaveAndRestore()

    assertContentEquals(listOf(1.0, 2.0, 2.0), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
    source.update(3.0)
    awaitIdle()
    assertContentEquals(listOf(1.0, 2.0, 2.0, 3.0), values)
    assertContentEquals(listOf(false, true, false), disposedValues)
  }
}
