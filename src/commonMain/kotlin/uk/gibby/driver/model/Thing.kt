package uk.gibby.driver.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure


/**
 * Thing
 *
 * A helper class for representing a reference to a Thing.
 * This is used to represent a reference to a Thing in a record class.
 * This is useful when using the 'FETCH' statement as when a Thing is fetched it is returned as an [Record] rather than a [Reference].
 *
 * @param T the type of the thing
 * @property id the id of the thing
 */
@Serializable(with = ThingSerializer::class)
sealed class Thing<T> {
    abstract val id: String
    data class Reference<T>(override val id: String): Thing<T>()
    data class Record<T>(override val id: String, val result: T): Thing<T>()
}

/**
 * Unknown
 *
 * A helper method for a creating a blank Thing.
 * This should be used as the default value for the id field of a record class.
 *
 * @param T the type of the thing
 * @return a blank Thing
 */
fun <T>unknown(): Thing<T> = Thing.Reference("unknown")

class ThingSerializer<T: Any>(
    private val tSerializer: KSerializer<T>
) : KSerializer<Thing<T>> {

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(tSerializer.descriptor.serialName + "Link") {
        element("id", String.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Thing<T> {
        return try {
            val result = decoder.decodeSerializableValue(tSerializer)
            var id: String? = null
            decoder.decodeStructure(descriptor) {
                id = decodeStringElement(descriptor, 0)
            }
            Thing.Record(id!!, result)
        } catch (e: Exception) {
            Thing.Reference(decoder.decodeString())
        }
    }

    override fun serialize(encoder: Encoder, value: Thing<T>) {
        when(value) {
            is Thing.Reference -> encoder.encodeString(value.id)
            is Thing.Record -> {
                encoder.encodeSerializableValue(tSerializer, value.result)
            }
        }
    }
}
