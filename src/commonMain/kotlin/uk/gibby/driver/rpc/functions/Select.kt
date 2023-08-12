package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

/**
 * Select
 *
 * Select all records in a table.
 *
 * @return The records in the table
 */
@JvmName("selectJson")
suspend fun Surreal.select(table: String): JsonArray {
    return sendRequest("select", buildJsonArray { add(table) }) as JsonArray
}

/**
 * Select
 *
 * Select all records in a table.
 *
 * @param T The type of the records
 * @return The records in the table
 */
suspend inline fun <reified T>Surreal.select(table: String): List<T> {
    val response = select(table)
    return surrealJson.decodeFromJsonElement(response)
}

/**
 * Select
 *
 * Select a specific record in a table.
 *
 * @param table The table to select the record from
 * @param id The id of the record to select
 * @return The record in the table
 */
@JvmName("selectIdJson")
suspend fun Surreal.select(table: String, id: String): JsonObject {
    return sendRequest("select", buildJsonArray { add("$table:$id") }) as JsonObject
}


/**
 * Select
 *
 * Select a specific record in a table.
 *
 * @param table The table to select the record from
 * @param id The id of the record to select
 * @param T The type of the record
 * @return The record in the table
 */
suspend inline fun <reified T>Surreal.select(table: String, id: String): T {
    val response = select(table, id)
    return surrealJson.decodeFromJsonElement(response)
}
