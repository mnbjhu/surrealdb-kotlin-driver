package uk.gibby.driver.model.rpc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import uk.gibby.driver.model.Thing
import uk.gibby.driver.model.ThingSerializer

class LiveQueryActionSerializer<T: Any>(private val resultSerializer: KSerializer<T>): KSerializer<LiveQueryAction<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LiveQueryAction") {
        element("action", String.serializer().descriptor)
        element("id", String.serializer().descriptor)
        element("result", resultSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): LiveQueryAction<T> {
        val input = decoder.beginStructure(descriptor)
        var action: String? = null
        var id: String? = null
        var result: Thing<T>? = null
        loop@ while (true) {
            when (val i = input.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> action = input.decodeStringElement(descriptor, i)
                1 -> id = input.decodeStringElement(descriptor, i)
                2 -> result = input.decodeSerializableElement(descriptor, i, ThingSerializer(resultSerializer))
                else -> throw SerializationException("Unknown index $i")
            }
        }
        input.endStructure(descriptor)
        if (action == null || id == null || result == null) throw SerializationException("Missing fields")
        return when(action) {
            "CREATE" -> LiveQueryAction.Create(id, (result as Thing.Record<T>).result)
            "UPDATE" -> LiveQueryAction.Update(id, (result as Thing.Record<T>).result)
            "DELETE" -> LiveQueryAction.Delete(id, (result as Thing.Reference).id)
            else -> throw SerializationException("Unknown action $action")
        }
    }

    override fun serialize(encoder: Encoder, value: LiveQueryAction<T>) {
        TODO("Not yet implemented")
    }

}