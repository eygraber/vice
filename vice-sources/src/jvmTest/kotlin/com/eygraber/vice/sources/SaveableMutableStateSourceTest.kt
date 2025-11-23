package com.eygraber.vice.sources

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

@OptIn(ExperimentalTestApi::class)
class SaveableMutableStateSourceTest {
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
    val simpleSource = SimpleSource()
    val values = mutableSetOf<Int>()
    val disposedValues = mutableListOf<Boolean>()
    val restorationTester = ViceStateRestorationTester(this)

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
    val complexSource = ComplexSource()
    val values = mutableSetOf<ComplexData>()
    val disposedValues = mutableListOf<Boolean>()
    val restorationTester = ViceStateRestorationTester(this)

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

  private class SimpleSource : SaveableMutableStateSource<Int>() {
    override val initial: Int = 1

    fun increment() {
      update(value + 1)
    }
  }

  private data class ComplexData(val data: Int)

  private class ComplexSource : SaveableMutableStateSource<ComplexData>(
    saver = Saver(
      save = {
        it.data
      },
      restore = {
        ComplexData(it)
      },
    ),
  ) {
    override val initial: ComplexData = ComplexData(1)

    fun increment() {
      update(ComplexData(value.data + 1))
    }
  }
}
