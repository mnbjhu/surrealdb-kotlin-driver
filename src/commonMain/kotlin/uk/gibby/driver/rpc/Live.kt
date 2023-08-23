package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.annotation.SurrealDbNightlyOnlyApi
import uk.gibby.driver.model.Thing
import uk.gibby.driver.surrealJson


@SurrealDbNightlyOnlyApi
suspend fun Surreal.live(table: String): String {
    val response = sendRequest("live", buildJsonArray { add(table) })
    return surrealJson.decodeFromJsonElement(response)
}

@SurrealDbNightlyOnlyApi
suspend fun Surreal.live(table: String, id: String): String {
    val response = sendRequest("live", buildJsonArray { add("$table:$id") })
    return surrealJson.decodeFromJsonElement(response)
}

@SurrealDbNightlyOnlyApi
suspend fun Surreal.live(id: Thing<*>): String {
    val response = sendRequest("live", buildJsonArray { add(id.id) })
    return surrealJson.decodeFromJsonElement(response)
}