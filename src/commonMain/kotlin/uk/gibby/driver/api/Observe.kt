package uk.gibby.driver.api

import kotlinx.serialization.json.JsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.annotation.SurrealDbNightlyOnlyApi
import uk.gibby.driver.model.query.data
import uk.gibby.driver.model.rpc.LiveQueryAction
import uk.gibby.driver.rpc.live
import uk.gibby.driver.model.rpc.asType
import uk.gibby.driver.rpc.query
import kotlin.jvm.JvmName

@JvmName("observeJson")
@SurrealDbNightlyOnlyApi
suspend fun Surreal.observeLiveQueryAsJson(table: String): LiveQueryFlow<JsonElement> {
    val liveQueryId = live(table)
    return LiveQueryFlow(
        flow = subscribeToTableAsJson(liveQueryId),
        id = liveQueryId,
        connection = this
    )
}


@SurrealDbNightlyOnlyApi
suspend inline fun <reified T>Surreal.observeLiveQuery(table: String): LiveQueryFlow<T> {
    val jsonFlow = observeLiveQueryAsJson(table)
    return jsonFlow.map { it.asType<T>() }
}
