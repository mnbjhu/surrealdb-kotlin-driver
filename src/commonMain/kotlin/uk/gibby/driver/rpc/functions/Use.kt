package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson

suspend fun Surreal.use(ns: String, db: String): JsonElement {
    val result = sendRequest("use", buildJsonArray { add(ns); add(db) })
    return surrealJson.decodeFromJsonElement(result)
}

