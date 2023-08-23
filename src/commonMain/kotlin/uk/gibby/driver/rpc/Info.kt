package uk.gibby.driver.rpc

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

/**
 * Info
 *
 * This method returns the record of an authenticated scope user as Json.
 *
 * @return The record of the authenticated scope user
 */
@JvmName("JsonInfo")
suspend fun Surreal.infoAsJson(): JsonElement {
    return sendRequest("info", buildJsonArray {  })
}

/**
 * Info
 *
 * This method returns the record of an authenticated scope user.
 *
 * @param T The type of the record
 * @return The record of the authenticated scope user
 */
suspend inline fun <reified T> Surreal.info(): T {
    val response = infoAsJson()
    return surrealJson.decodeFromJsonElement(response)
}
