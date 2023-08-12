package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.encodeToJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

@JvmName("letJson")
suspend fun Surreal.let(name: String, value: JsonElement) {
    sendRequest("let", buildJsonArray { add(name); add(value) })
}

suspend inline fun <reified T> Surreal.let(name: String, value: T) {
    let(name, surrealJson.encodeToJsonElement(value))
}
