package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.rpc.model.RootAuth
import uk.gibby.driver.surrealJson


suspend fun Surreal.signIn(user: String, pass: String): String? {
    val result = sendRequest("signin", surrealJson.encodeToJsonElement(listOf(RootAuth(user, pass))) as JsonArray)
    return surrealJson.decodeFromJsonElement(result)
}

suspend fun Surreal.signIn(ns: String, db: String, scope: String, params: List<Bind>): String {
    val auth = buildJsonObject {
        put("NS", ns)
        put("DB", db)
        put("SC", scope)
        params.forEach {
            put(it.first, it.second)
        }
    }
    val result = sendRequest("signin", buildJsonArray { add(auth) })
    return surrealJson.decodeFromJsonElement(result)
}

suspend fun Surreal.signIn(ns: String, db: String, scope: String, vararg params: Bind): String {
    return signIn(ns, db, scope, params.toList())
}