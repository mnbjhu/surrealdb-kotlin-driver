package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

@JvmName("selectJson")
suspend fun Surreal.select(table: String): JsonArray {
    return sendRequest("select", buildJsonArray { add(table) }) as JsonArray
}

suspend inline fun <reified T>Surreal.select(table: String): List<T> {
    val response = select(table)
    return surrealJson.decodeFromJsonElement(response)
}

@JvmName("selectIdJson")
suspend fun Surreal.select(table: String, id: String): JsonObject {
    return sendRequest("select", buildJsonArray { add("$table:$id") }) as JsonObject
}

suspend inline fun <reified T>Surreal.select(table: String, id: String): T {
    val response = select(table, id)
    return surrealJson.decodeFromJsonElement(response)
}
