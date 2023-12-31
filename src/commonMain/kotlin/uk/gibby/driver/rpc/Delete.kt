package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.Thing

/**
 * Delete
 *
 * Deletes all records in a table
 *
 * @param table The table to delete
 */
suspend fun Surreal.delete(table: String) {
    sendRequest("delete", buildJsonArray { add(table) })
}

/**
 * Delete
 *
 * Deletes a specific record in a table
 *
 * @param table The table to delete the record from
 * @param id The id of the record to delete
 */
suspend fun Surreal.delete(table: String, id: String) {
    sendRequest("delete", buildJsonArray { add("$table:$id") })
}

/**
 * Delete
 *
 * Deletes a specific record in a table
 *
 * @param id The id of the record to delete
 */
suspend fun Surreal.delete(id: Thing<*>) {
    sendRequest("delete", buildJsonArray { add(id.id) })
}
