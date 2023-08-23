package uk.gibby.driver.rpc

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.encodeToJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

/**
 * Let
 *
 * This method stores a variable on the current connection
 *
 * @param name The name of the variable without a prefixed `$` character
 * @param value The value for the variable
 */
@JvmName("letJson")
suspend fun Surreal.let(name: String, value: JsonElement) {
    sendRequest("let", buildJsonArray { add(name); add(value) })
}

/**
 * Let
 *
 * This method stores a variable on the current connection
 *
 * @param name The name of the variable without a prefixed `$` character
 * @param value The value for the variable
 */
suspend inline fun <reified T> Surreal.let(name: String, value: T) {
    let(name, surrealJson.encodeToJsonElement(value))
}
