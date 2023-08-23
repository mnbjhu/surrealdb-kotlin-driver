package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal
import uk.gibby.driver.annotation.SurrealDbNightlyOnlyApi

/**
 * Kill
 *
 * This method will kill a live query
 *
 * @param liveQueryId The id of the live query to kill
 */
@SurrealDbNightlyOnlyApi
suspend fun Surreal.kill(liveQueryId: String) {
    sendRequest("kill", buildJsonArray { add(liveQueryId) })
}

