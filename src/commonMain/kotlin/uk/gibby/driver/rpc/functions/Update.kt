package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.rpc.model.JsonPatch
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

class UpdateBuilder(private val table: String, private val db: Surreal) {

    @JvmName("contentJsonElement")
    suspend fun content(data: JsonElement): JsonArray {
        return db.sendRequest("update", buildJsonArray {
            add(table)
            add(data)
        }) as JsonArray
    }

    suspend inline fun <reified T>content(data: T): List<T> {
        val response = content(surrealJson.encodeToJsonElement(data))
        return surrealJson.decodeFromJsonElement(response)
    }

    @JvmName("mergeJsonElement")
    suspend fun merge(data: JsonObject): JsonArray {
        return db.sendRequest("merge", buildJsonArray {
            add(table)
            add(data)
        }) as JsonArray
    }

    suspend inline fun <reified T>merge(data: JsonObject): List<T> {
        val response = merge(data)
        return surrealJson.decodeFromJsonElement(response)
    }

    @JvmName("mergeBindJson")
    suspend fun merge(vararg data: Bind): JsonArray {
        return db.sendRequest("merge", buildJsonArray {
            add(table)
            add(
                buildJsonObject {
                    data.forEach {
                        put(it.first, it.second)
                    }
                }
            )
        }) as JsonArray
    }

    @JvmName("mergeBind")
    suspend inline fun <reified T>merge(vararg data: Bind): List<T> {
        val response = merge(*data)
        return surrealJson.decodeFromJsonElement(response)
    }

    suspend fun patch(patchBuilder: JsonPatch.Builder.() -> Unit): List<List<JsonPatch>> {
        val builder = JsonPatch.Builder()
        builder.patchBuilder()
        val response = db.sendRequest("patch", buildJsonArray {
            add(table)
            add(surrealJson.encodeToJsonElement(builder.build()))
        })
        return surrealJson.decodeFromJsonElement(response)
    }

}

fun Surreal.update(table: String) = UpdateBuilder(table, this)

class UpdateIdBuilder(private val table: String, private val id: String, private val db: Surreal) {

    @JvmName("contentJsonElement")
    suspend fun content(data: JsonElement): JsonObject {
        return db.sendRequest("update", buildJsonArray {
            add("$table:$id")
            add(data)
        }) as JsonObject
    }

    suspend inline fun <reified T>content(data: T): T {
        val response = content(surrealJson.encodeToJsonElement(data))
        return surrealJson.decodeFromJsonElement(response)
    }

    @JvmName("mergeJson")
    suspend fun merge(data: JsonObject): JsonObject {
        return db.sendRequest("merge", buildJsonArray {
            add("$table:$id")
            add(data)
        }) as JsonObject
    }

    suspend inline fun <reified T>merge(data: JsonObject): T {
        val response = merge(data)
        return surrealJson.decodeFromJsonElement(response)
    }

    @JvmName("mergeBindJson")
    suspend fun merge(vararg data: Bind): JsonObject {
        return db.sendRequest("merge", buildJsonArray {
            add("$table:$id")
            add(
                buildJsonObject {
                    data.forEach {
                        put(it.first, it.second)
                    }
                }
            )
        }) as JsonObject
    }

    @JvmName("mergeBind")
    suspend inline fun <reified T>merge(vararg data: Bind): T {
        val response = merge(*data)
        return surrealJson.decodeFromJsonElement(response)
    }

    suspend fun patch(patchBuilder: JsonPatch.Builder.() -> Unit): List<JsonPatch> {
        val builder = JsonPatch.Builder()
        builder.patchBuilder()
        val result = db.sendRequest("patch", buildJsonArray {
            add("$table:$id")
            add(surrealJson.encodeToJsonElement(builder.build()))
        })
        return surrealJson.decodeFromJsonElement(result)
    }

}

fun Surreal.update(table: String, id: String) = UpdateIdBuilder(table, id, this)
