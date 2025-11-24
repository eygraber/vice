package com.eygraber.vice.sources.mutable.source

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.eygraber.vice.sources.ViceStateRestorationTester
import com.eygraber.vice.sources.serializableMutableViceSource
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SerializableMutableViceSourceTest {
  @Test
  fun `test that initial value is the correct value`() = runComposeUiTest {
    val source = serializableMutableViceSource(
      initial = ComplexData(data = 1),
      serializer = ComplexDataSerializer,
    )
    var value: ComplexData? = null
    setContent {
      value = source.currentState()
    }
    assertEquals(ComplexData(1), value)
  }

  @Test
  fun `test that updates work correctly`() = runComposeUiTest {
    val source = serializableMutableViceSource(
      initial = ComplexData(data = 0),
      serializer = ComplexDataSerializer,
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
  fun `test that serializable mutable vice source is retained across recreation`() = runComposeUiTest {
    var source = serializableMutableViceSource(
      initial = ComplexData(1),
      serializer = ComplexDataSerializer,
    )
    val values = mutableListOf<ComplexData>()
    val restorationTester = ViceStateRestorationTester(
      composeTest = this,
      onDisposedAction = {
        source = serializableMutableViceSource(
          initial = ComplexData(1),
          serializer = ComplexDataSerializer,
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

  private object ComplexDataSerializer : KSerializer<ComplexData> {
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
  }
}
