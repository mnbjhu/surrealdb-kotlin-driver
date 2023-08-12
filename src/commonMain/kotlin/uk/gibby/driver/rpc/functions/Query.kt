package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.rpc.model.QueryResponse
import uk.gibby.driver.surrealJson

suspend fun Surreal.query(queryText: String, bindings: List<Bind>): List<QueryResponse> {
    val result = sendRequest("query", buildJsonArray {
        add(queryText)
        add(buildJsonObject {
            bindings.forEach { (name, value) -> put(name, value) }
        })
    })
    return surrealJson.decodeFromJsonElement(result)
}

suspend fun Surreal.query(queryText: String, vararg bindings: Bind): List<QueryResponse> {
    return query(queryText, bindings.toList())
}