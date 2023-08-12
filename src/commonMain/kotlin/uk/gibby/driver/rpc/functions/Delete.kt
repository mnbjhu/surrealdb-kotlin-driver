package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

suspend fun Surreal.delete(table: String) {
    sendRequest("delete", buildJsonArray { add(table) })
}

suspend fun Surreal.delete(table: String, id: String) {
    sendRequest("delete", buildJsonArray { add("$table:$id") })
}