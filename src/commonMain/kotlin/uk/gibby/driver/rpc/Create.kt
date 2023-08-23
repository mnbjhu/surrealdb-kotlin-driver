package uk.gibby.driver.rpc

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson

/**
 * Create builder
 *
 * A builder for creating a new record with a random id.
 * Should be used in conjunction with [jsonContent] or [content]
 *
 * @property table The table to create the record in
 * @property db The database connection to use
 * @constructor Creates builder with the specified table and database connection
 */
class CreateBuilder(private val table: String, private val db: Surreal) {


    /**
     * Json content
     *
     * Sets the content of the record to be created and executes the request
     *
     * @param data The data to set the record to
     * @return The newly created record
     */
    suspend fun jsonContent(data: JsonObject): JsonObject {
        val response = db.sendRequest("create", buildJsonArray { add(table); add(data) })
        return surrealJson.decodeFromJsonElement<List<JsonObject>>(response)[0]
    }

    /**
     * Content
     *
     * Sets the content of the record to be created and executes the request
     *
     * @param T The type of the data to set the record to
     * @param data The data to set the record to
     * @return The newly created record
     */
    suspend inline fun <reified T> content(data: T): T {
        val response = jsonContent(surrealJson.encodeToJsonElement(data) as JsonObject)
        return surrealJson.decodeFromJsonElement(response)
    }
}

/**
 * Create id builder
 *
 * A builder for creating a new record with a specific id.
 * Should be used in conjunction with [jsonContent] or [content]
 *
 * @property thing The id of the record to create
 * @property db The database connection to use
 * @constructor Creates builder with the specified id and database connection
 */
class CreateIdBuilder(private val thing: String, private val db: Surreal) {


    /**
     * Json content
     *
     * Sets the content of the record to be created and executes the request
     *
     * @param data The data to set the record to
     * @return The newly created record
     */
    suspend fun jsonContent(data: JsonObject): JsonObject {
        val response = db.sendRequest("create", buildJsonArray { add(thing); add(data) })
        return surrealJson.decodeFromJsonElement<JsonObject>(response)
    }

    /**
     * Content
     *
     * Sets the content of the record to be created and executes the request
     *
     * @param T The type of the data to set the record to
     * @param data The data to set the record to
     * @return The newly created record
     */
    suspend inline fun <reified T> content(data: T): T {
        val response = jsonContent(surrealJson.encodeToJsonElement(data) as JsonObject)
        return surrealJson.decodeFromJsonElement(response)
    }
}

/**
 * Create
 *
 * Create a builder for creating a new record with a random id.
 * Should be used in conjunction with [CreateBuilder.jsonContent] or [CreateBuilder.content]
 *
 * @param table The table to create the record in
 * @return The builder
 */
fun Surreal.create(table: String) = CreateBuilder(table, this)

/**
 * Create
 *
 * Create a builder for creating a new record with a specific id.
 * Should be used in conjunction with [CreateIdBuilder.jsonContent] or [CreateIdBuilder.content]
 *
 * @param table The table to create the record in
 * @param id The id of the record to create
 * @return The builder
 */
fun Surreal.create(table: String, id: String) = CreateIdBuilder("$table:$id", this)
