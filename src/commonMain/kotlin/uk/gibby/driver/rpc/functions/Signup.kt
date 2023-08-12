package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.*
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.model.Bind
import uk.gibby.driver.surrealJson


/**
 * Signup
 *
 * This method allows you to signup a user against a scope's SIGNUP method
 *
 * @param ns Specifies the namespace of the scope
 * @param db Specifies the database of the scope
 * @param scope Specifies the scope
 * @param params A set of variables used to authenticate
 * @return The authentication token for the user session
 */
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

/**
 * Signup
 *
 * This method allows you to signup a user against a scope's SIGNUP method
 *
 * @param ns Specifies the namespace of the scope
 * @param db Specifies the database of the scope
 * @param scope Specifies the scope
 * @param params A set of variables used to authenticate
 * @return The authentication token for the user session
 */
suspend fun Surreal.signup(ns: String, db: String, scope: String, vararg params: Bind): String {
    return signup(ns, db, scope, params.toList())
}