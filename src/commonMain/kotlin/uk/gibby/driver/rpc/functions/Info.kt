package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.Surreal
import uk.gibby.driver.surrealJson
import kotlin.jvm.JvmName

@JvmName("JsonInfo")
suspend fun Surreal.info(): JsonElement {
    return sendRequest("info", buildJsonArray {  })
}

suspend inline fun <reified T> Surreal.info(): T {
    val response = info()
    return surrealJson.decodeFromJsonElement(response)
}
