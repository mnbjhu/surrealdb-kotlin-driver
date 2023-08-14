package uk.gibby.driver.rpc.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class LiveQueryAction(val action: String, val id: String, val result: JsonObject? = null)