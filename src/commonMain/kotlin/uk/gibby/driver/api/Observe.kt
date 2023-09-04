package uk.gibby.driver.api

import kotlinx.serialization.json.JsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.live
import uk.gibby.driver.model.rpc.asType
import kotlin.jvm.JvmName

/**
 * Observe live query as json
 *
 * Creates a [LiveQueryFlow] for the given table.
 *
 * @param table Name of the table to 'LIVE SELECT' from
 * @return A [LiveQueryFlow] of [JsonElement]s
 */
@JvmName("observeJson")
suspend fun Surreal.observeLiveQueryAsJson(table: String): LiveQueryFlow<JsonElement> {
    val liveQueryId = live(table)
    return LiveQueryFlow(
        flow = subscribeAsJson(liveQueryId),
        id = liveQueryId,
        connection = this
    )
}


/**
 * Observe live query
 *
 * Creates a [LiveQueryFlow] for the given table.
 *
 * @param T The type of the records
 * @param table Name of the table to 'LIVE SELECT' from
 * @return A [LiveQueryFlow] of [T]s
 */
suspend inline fun <reified T>Surreal.observeLiveQuery(table: String): LiveQueryFlow<T> {
    val jsonFlow = observeLiveQueryAsJson(table)
    return jsonFlow.map { it.asType<T>() }
}
