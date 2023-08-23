package uk.gibby.driver.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class RootAuth(val user: String, val pass: String)
