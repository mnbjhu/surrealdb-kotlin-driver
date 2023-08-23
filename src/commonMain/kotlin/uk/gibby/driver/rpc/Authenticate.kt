package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

/**
 * Authenticate
 *
 * This method allows you to authenticate a user against SurrealDB with a token
 *
 * @param token The token to authenticate with
 */
suspend fun Surreal.authenticate(token: String) {
    sendRequest("authenticate", buildJsonArray { add(token) })
}