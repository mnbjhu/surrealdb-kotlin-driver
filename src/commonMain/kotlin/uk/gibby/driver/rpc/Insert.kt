package uk.gibby.driver.rpc

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson

class InsertBuilder(private val table: String, private val db: Surreal) {

    suspend fun jsonContent(data: List<JsonObject>): JsonArray {
        val response = db.sendRequest("insert", buildJsonArray {
            add(table)
            addJsonArray { data.forEach { add(it) } }
        })
        return response as JsonArray
    }

    suspend inline fun <reified T> content(data: List<T>): List<T> {
        val response = jsonContent(data.map { surrealJson.encodeToJsonElement(it) as JsonObject })
        return surrealJson.decodeFromJsonElement(response)
    }

    suspend inline fun <reified T> content(vararg data: T): List<T> {
        val response = jsonContent(data.map { surrealJson.encodeToJsonElement(it) as JsonObject })
        return surrealJson.decodeFromJsonElement(response)
    }
}

fun Surreal.insert(table: String): InsertBuilder {
    return InsertBuilder(table, this)
}