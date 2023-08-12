package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.surrealJson

suspend fun Surreal.signup(ns: String, db: String, scope: String, params: List<Bind>): String {
    val auth = buildJsonObject {
        put("NS", ns)
        put("DB", db)
        put("SC", scope)
        params.forEach {
            put(it.first, it.second)
        }
    }
    val result = sendRequest("signup", buildJsonArray { add(auth) })
    return surrealJson.decodeFromJsonElement(result)
}

suspend fun Surreal.signup(ns: String, db: String, scope: String, vararg params: Bind): String {
    return signup(ns, db, scope, params.toList())
}