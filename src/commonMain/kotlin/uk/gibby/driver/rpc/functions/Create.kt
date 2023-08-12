package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson

class CreateBuilder(internal val table: String, internal val db: Surreal)
class CreateIdBuilder(internal val thing: String, internal val db: Surreal)

fun Surreal.create(table: String) = CreateBuilder(table, this)

fun Surreal.create(table: String, id: String) = CreateIdBuilder("$table:$id", this)

suspend fun CreateBuilder.jsonContent(data: JsonObject): JsonObject {
    val response = db.sendRequest("create", buildJsonArray { add(table); add(data) })
    return surrealJson.decodeFromJsonElement<List<JsonObject>>(response)[0]
}

suspend inline fun <reified T> CreateBuilder.content(data: T): T {
    val response = jsonContent(surrealJson.encodeToJsonElement(data) as JsonObject)
    return surrealJson.decodeFromJsonElement(response)
}

suspend fun CreateIdBuilder.jsonContent(data: JsonObject): JsonObject {
    val response = db.sendRequest("create", buildJsonArray { add(thing); add(data) })
    return surrealJson.decodeFromJsonElement<JsonObject>(response)
}

suspend inline fun <reified T> CreateIdBuilder.content(data: T): T {
    val response = jsonContent(surrealJson.encodeToJsonElement(data) as JsonObject)
    return surrealJson.decodeFromJsonElement(response)
}
