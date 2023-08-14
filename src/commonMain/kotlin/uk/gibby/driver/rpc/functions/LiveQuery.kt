package uk.gibby.driver.rpc.functions

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.LiveQueryAction
import uk.gibby.driver.surrealJson

suspend fun Surreal.live(table: String): Flow<LiveQueryAction> {
    val response = sendRequest("live", buildJsonArray{ add(table) })
    val id: String = surrealJson.decodeFromJsonElement(response)
    return subscribe(id).consumeAsFlow()
}