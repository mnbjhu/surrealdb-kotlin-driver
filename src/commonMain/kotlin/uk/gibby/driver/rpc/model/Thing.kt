package uk.gibby.driver.rpc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@Serializable(with = ThingSerializer::class)
sealed class Thing<T> {
    abstract val id: String
    data class Reference<T>(override val id: String): Thing<T>()
    data class Actual<T>(override val id: String, val result: T): Thing<T>()
}

class ThingSerializer<T: Any>(
    private val tSerializer: KSerializer<T>
) : KSerializer<Thing<T>> {

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(tSerializer.descriptor.serialName + "Link") {
        element("id", String.serializer().descriptor)
        tSerializer.descriptor.elementNames.forEachIndexed { index, name ->
            val descriptor = tSerializer.descriptor.getElementDescriptor(index)
            element(name, descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): Thing<T> {
        return try {
            val result = decoder.decodeSerializableValue(tSerializer)
            var id: String? = null
            decoder.decodeStructure(descriptor) {
                id = decodeStringElement(descriptor, 0)
            }
            Thing.Actual(id!!, result)
        } catch (e: Exception) {
            Thing.Reference(decoder.decodeString())
        }
    }

    override fun serialize(encoder: Encoder, value: Thing<T>) {
        when(value) {
            is Thing.Reference -> encoder.encodeString(value.id)
            is Thing.Actual -> {
                encoder.encodeSerializableValue(tSerializer, value.result)
            }
        }
    }
}
