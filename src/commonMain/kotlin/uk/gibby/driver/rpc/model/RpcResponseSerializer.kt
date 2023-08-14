package uk.gibby.driver.rpc.model

import uk.gibby.driver.surrealJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

object RpcResponseSerializer: KSerializer<RpcResponse> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("rpc") {
        element("id", String.serializer().descriptor)
        element("result", JsonElement.serializer().descriptor)
        element("error", JsonElement.serializer().descriptor)
    }
    override fun deserialize(decoder: Decoder): RpcResponse {
        var id: String? = null
        var result: JsonElement? = null
        var error: JsonElement? = null
        decoder.decodeStructure(descriptor){
            while (true) {
                when(decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> result = decodeSerializableElement(descriptor, 1, JsonElement.serializer())
                    2 -> error = decodeSerializableElement(descriptor, 2, JsonElement.serializer())
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
        }
        return if(error != null) {
            RpcResponse.Error(id!!, surrealJson.encodeToJsonElement(error))
        } else if(id != null) {
            RpcResponse.Success(id!!, result ?: surrealJson.encodeToJsonElement(String.serializer().nullable, null))
        }
        else {
            RpcResponse.Notification(surrealJson.decodeFromJsonElement(result!!))
        }
    }

    override fun serialize(encoder: Encoder, value: RpcResponse) {
        TODO("Not yet implemented")
    }

}