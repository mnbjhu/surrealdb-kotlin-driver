package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson


/**
 * Live
 *
 * This method will create a live query
 *
 * @param table Name of the table to 'LIVE SELECT' from
 * @return The id of the live query
 */
suspend fun Surreal.live(table: String): String {
    val response = sendRequest("live", buildJsonArray { add(table) })
    return surrealJson.decodeFromJsonElement(response)
}