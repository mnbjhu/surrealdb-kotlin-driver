package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.rpc.model.RootAuth
import uk.gibby.driver.surrealJson


/**
 * Signin
 *
 * This method allows you to signin SurrealDB as root user
 *
 * @param user The username to authenticate with
 * @param pass The password to authenticate with
 */
suspend fun Surreal.signin(user: String, pass: String) {
    sendRequest("signin", surrealJson.encodeToJsonElement(listOf(RootAuth(user, pass))) as JsonArray)
}

/**
 * Signin
 *
 * This method allows you to signin SurrealDB as scoped user
 *
 * @param ns The namespace to sign in to
 * @param db The database to sign in to
 * @param scope The scope to sign in to
 * @param params A set of variables used to authenticate
 * @return The authentication token for the user session
 */
suspend fun Surreal.signin(ns: String, db: String, scope: String, params: List<Bind>): String {
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

/**
 * Signin
 *
 * This method allows you to signin SurrealDB as scoped user
 *
 * @param ns The namespace to sign in to
 * @param db The database to sign in to
 * @param scope The scope to sign in to
 * @param params A set of variables used to authenticate
 * @return The authentication token for the user session
 */
suspend fun Surreal.signin(ns: String, db: String, scope: String, vararg params: Bind): String {
    return signin(ns, db, scope, params.toList())
}