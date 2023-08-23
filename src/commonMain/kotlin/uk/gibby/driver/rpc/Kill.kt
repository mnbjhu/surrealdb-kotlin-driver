package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

suspend fun Surreal.kill(liveQueryId: String) {
    sendRequest("kill", buildJsonArray { add(liveQueryId) })
}

