package com.eygraber.vice.sources

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.test.Test
import kotlin.test.assertContentEquals

@OptIn(ExperimentalTestApi::class)
class SerializableMutableStateSourceTest {
  @Test
  fun `test that initial value is the correct value`() {
    assert(ComplexSource().value == ComplexData(1))
  }

  @Test
  fun `test that complex serializable mutable state source works`() = runComposeUiTest {
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
  fun `test that complex serializable mutable state source works across recreation`() = runComposeUiTest {
    val complexSource = ComplexSource()
    val values = mutableSetOf<ComplexData>()
    val restorationTester = ViceStateRestorationTester(this)
    val disposedValues = mutableListOf<Boolean>()

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

  private data class ComplexData(val data: Int)

  private class ComplexSource : SerializableMutableStateSource<ComplexData>(
    stateSerializer = object : KSerializer<ComplexData> {
      override val descriptor: SerialDescriptor = Int.serializer().descriptor

      override fun serialize(
        encoder: Encoder,
        value: ComplexData,
      ) {
        encoder.encodeInt(value.data)
      }

      override fun deserialize(
        decoder: Decoder,
      ) = ComplexData(decoder.decodeInt())
    },
  ) {
    override val initial: ComplexData = ComplexData(1)

    fun increment() {
      update(ComplexData(value.data + 1))
    }
  }
}
