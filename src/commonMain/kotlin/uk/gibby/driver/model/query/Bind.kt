package uk.gibby.driver.model.query

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import uk.gibby.driver.surrealJson

typealias Bind = Pair<String, JsonElement>

/**
 * Bind
 *
 * A helper function to create a parameter as part of a query.
 * Used in conjunction with [uk.gibby.driver.rpc.functions.query], [uk.gibby.driver.rpc.functions.update], [uk.gibby.driver.rpc.functions.signin] and [uk.gibby.driver.rpc.functions.signup]
 *
 * @param T The type of the value to bind
 * @param name The name of the parameter without a prefixed `$` character
 * @param value The value to bind
 * @return The named parameter
 */
inline fun <reified T> bind(name: String, value: T): Bind {
    return Bind(name, surrealJson.encodeToJsonElement(value))
}