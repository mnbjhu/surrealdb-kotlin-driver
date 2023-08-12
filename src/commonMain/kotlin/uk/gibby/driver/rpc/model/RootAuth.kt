package uk.gibby.driver.rpc.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class RootAuth(val user: String, val pass: String)

@Serializable
data class ScopeAuth(val NS: String, val DB: String, val SC: String, val creds: JsonElement)
