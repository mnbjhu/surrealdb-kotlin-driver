package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.JsonArray
import uk.gibby.driver.Surreal

/**
 * Invalidate
 *
 * This method will invalidate the user's session for the current connection
 */
suspend fun Surreal.invalidate() {
    sendRequest("invalidate", JsonArray(listOf()))
}
