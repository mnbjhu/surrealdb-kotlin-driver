package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

suspend fun Surreal.authenticate(token: String) {
    sendRequest("authenticate", buildJsonArray { add(token) })
}