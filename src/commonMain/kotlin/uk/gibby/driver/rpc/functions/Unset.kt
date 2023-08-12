package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

suspend fun Surreal.unset(name: String) {
    sendRequest("unset", buildJsonArray { add(name) })
}
