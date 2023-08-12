package uk.gibby.driver.rpc.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class RpcRequest(val id: String, val method: String, val params: JsonArray)