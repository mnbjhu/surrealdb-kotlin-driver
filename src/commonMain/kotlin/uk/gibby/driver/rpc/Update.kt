package uk.gibby.driver.rpc

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.query.Bind
import uk.gibby.driver.model.JsonPatch
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName
import uk.gibby.driver.model.Thing


/**
 * Update builder
 *
 * This class is used to build update requests affecting all records in a table.
 *
 * @property table The table to update
 * @property db The database connection to use
 * @constructor Creates an update builder for the given table
 */
class UpdateBuilder(private val table: String, private val db: Surreal) {

    /**
     * Json content
     *
     * Updates all records in a table with the given Json content.
     *
     * @param data The Json content to update the records with
     * @return The updated records
     */
    suspend fun jsonContent(data: JsonElement): JsonArray {
        return db.sendRequest("update", buildJsonArray {
            add(table)
            add(data)
        }) as JsonArray
    }

    /**
     * Content
     *
     * Updates all records in a table with the given content.
     *
     * @param T The type of the content
     * @param data The content to update the records with
     * @return The updated records
     */
    suspend inline fun <reified T>content(data: T): List<T> {
        val response = jsonContent(surrealJson.encodeToJsonElement(data))
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Merge
     *
     * Merges all records in a table with the given Json content.
     *
     * @param data The Json content to merge the records with
     * @return The merged records
     */
    @JvmName("mergeJsonElement")
    suspend fun merge(data: JsonObject): JsonArray {
        return db.sendRequest("merge", buildJsonArray {
            add(table)
            add(data)
        }) as JsonArray
    }

    /**
     * Merge
     *
     * Merges all records in a table with the given content.
     *
     * @param T The type of the content
     * @param data The content to merge the records with
     * @return The merged records
     */
    suspend inline fun <reified T>merge(data: JsonObject): List<T> {
        val response = merge(data)
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Merge
     *
     * Merges all records in a table with the given parameters.
     *
     * @param data The parameters to merge the records with
     * @return The merged records
     */
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

    /**
     * Merge
     *
     * Merges all records in a table with the given parameters.
     *
     * @param T The type of the returned records
     * @param data The parameters to merge the records with
     * @return The merged records
     */
    @JvmName("mergeBind")
    suspend inline fun <reified T>merge(vararg data: Bind): List<T> {
        val response = merge(*data)
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Patch
     *
     * Patches all records in a table with the given Json patch.
     *
     * @param patchBuilder A json patch DSL to build the patch
     * @return The patches applied to the records
     */
    suspend fun patch(patchBuilder: JsonPatch.Builder.() -> Unit): List<List<JsonPatch>> {
        val builder = JsonPatch.Builder()
        builder.patchBuilder()
        val response = db.sendRequest("patch", buildJsonArray {
            add(table)
            add(surrealJson.encodeToJsonElement(builder.build()))
            add(true)
        })
        return surrealJson.decodeFromJsonElement(response)
    }

}

/**
 * Update
 *
 * Creates an update builder for the given table.
 *
 * @param table The table to update
 */
fun Surreal.update(table: String) = UpdateBuilder(table, this)

/**
 * Update id builder
 *
 * This class is used to build update requests affecting a single record in a table.
 *
 * @property table The table to update
 * @property id The id of the record to update
 * @property db The database connection to use
 * @constructor Creates an update builder for the given table and id
 */
class UpdateIdBuilder(private val id: String, private val db: Surreal) {

    /**
     * Content
     *
     * Updates a single record in a table with the given Json content.
     *
     * @param data The Json content to update the record with
     * @return The updated record
     */
    suspend fun jsonContent(data: JsonElement): JsonObject {
        return db.sendRequest("update", buildJsonArray {
            add(id)
            add(data)
        }) as JsonObject
    }

    /**
     * Content
     *
     * Updates a single record in a table with the given content.
     *
     * @param T The type of the content
     * @param data The content to update the record with
     * @return The updated record
     */
    suspend inline fun <reified T>content(data: T): T {
        val response = jsonContent(surrealJson.encodeToJsonElement(data))
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Merge
     *
     * Merges a single record in a table with the given Json content.
     *
     * @param data The Json content to merge the record with
     * @return The merged record
     */
    @JvmName("mergeJson")
    suspend fun merge(data: JsonObject): JsonObject {
        return db.sendRequest("merge", buildJsonArray {
            add(id)
            add(data)
        }) as JsonObject
    }

    /**
     * Merge
     *
     * Merges a single record in a table with the given content.
     *
     * @param T The type of the content
     * @param data The content to merge the record with
     * @return The merged record
     */
    suspend inline fun <reified T>merge(data: JsonObject): T {
        val response = merge(data)
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Merge
     *
     * Merges a single record in a table with the given parameters.
     *
     * @param data The parameters to merge the record with
     * @return The merged record
     */
    @JvmName("mergeBindJson")
    suspend fun merge(vararg data: Bind): JsonObject {
        return db.sendRequest("merge", buildJsonArray {
            add(id)
            add(
                buildJsonObject {
                    data.forEach {
                        put(it.first, it.second)
                    }
                }
            )
        }) as JsonObject
    }

    /**
     * Merge
     *
     * Merges a single record in a table with the given parameters.
     *
     * @param T The type of the returned record
     * @param data The parameters to merge the record with
     * @return The merged record
     */
    @JvmName("mergeBind")
    suspend inline fun <reified T>merge(vararg data: Bind): T {
        val response = merge(*data)
        return surrealJson.decodeFromJsonElement(response)
    }

    /**
     * Patch
     *
     * Patches a single record in a table with the given Json patch.
     *
     * @param patchBuilder A json patch DSL to build the patch
     * @return The patches applied to the record
     */
    suspend fun patch(patchBuilder: JsonPatch.Builder.() -> Unit): List<JsonPatch> {
        val builder = JsonPatch.Builder()
        builder.patchBuilder()
        val result = db.sendRequest("patch", buildJsonArray {
            add(id)
            add(surrealJson.encodeToJsonElement(builder.build()))
            add(true)
        })
        return surrealJson.decodeFromJsonElement(result)
    }

}

/**
 * Update
 *
 * Creates an update builder for the given table and id.
 *
 * @param table The table to update
 * @param id The id of the record to update
 */
fun Surreal.update(table: String, id: String) = UpdateIdBuilder("$table:$id", this)

/**
 * Update
 *
 * Creates an update builder for the given table and id.
 *
 * @param id The id of the record to update
 */
fun Surreal.update(id: Thing<*>) = UpdateIdBuilder(id.id, this)

