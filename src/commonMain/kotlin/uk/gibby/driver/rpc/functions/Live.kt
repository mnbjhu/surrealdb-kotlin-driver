package uk.gibby.driver.rpc.functions

import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.LiveQueryAction
import uk.gibby.driver.rpc.model.asType
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName


suspend fun Surreal.live(table: String): String {
    val response = sendRequest("live", buildJsonArray { add(table) })
    return surrealJson.decodeFromJsonElement(response)
}


@JvmName("observeJson")
suspend fun Surreal.observeAsJson(liveQueryId: String): LiveQueryFlow<JsonElement> {
    val id = live(liveQueryId)
    return LiveQueryFlow(
        flow = subscribe(id),
        id = id,
        connection = this
    )
}

suspend inline fun <reified T>Surreal.observe(table: String): LiveQueryFlow<T> {
    val jsonFlow = observeAsJson(table)
    return jsonFlow.map { it.asType<T>() }
}

class LiveQueryFlow<T>(
    private val flow: Flow<LiveQueryAction<T>>,
    val id: String,
    private val connection: Surreal
): Flow<LiveQueryAction<T>> by flow, Closeable {
    override fun close() {
        connection.unsubscribe(id)
        connection.triggerKill(id)
    }

    fun <R>map(transform: (LiveQueryAction<T>) -> LiveQueryAction<R>): LiveQueryFlow<R> {
        return LiveQueryFlow(
            flow = flow.map { transform(it) },
            id = id,
            connection = connection
        )
    }
}