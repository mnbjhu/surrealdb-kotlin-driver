package uk.gibby.driver.model.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable(with = RpcResponseSerializer::class)
sealed class RpcResponse {

    abstract val id: String?

    @Serializable
    data class Success(override val id: String, val result: JsonElement): RpcResponse()

    @Serializable
    data class Error(override val id: String, val error: JsonElement): RpcResponse()

    @Serializable
    data class Notification(val result: LiveQueryAction<JsonElement>): RpcResponse() {
        override val id: String? = null
    }
}

