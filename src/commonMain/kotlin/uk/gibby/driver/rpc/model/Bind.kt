package uk.gibby.driver.rpc.model

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import uk.gibby.driver.surrealJson

typealias Bind = Pair<String, JsonElement>

inline fun <reified T> bind(name: String, value: T): Bind {
    return Bind(name, surrealJson.encodeToJsonElement(value))
}