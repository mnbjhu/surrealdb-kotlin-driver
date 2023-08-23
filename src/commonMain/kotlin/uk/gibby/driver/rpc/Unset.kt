package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

/**
 * Unset
 *
 * This method removes a variable from the current connection
 *
 * @param name The name of the variable without a prefixed `$` character
 */
suspend fun Surreal.unset(name: String) {
    sendRequest("unset", buildJsonArray { add(name) })
}
