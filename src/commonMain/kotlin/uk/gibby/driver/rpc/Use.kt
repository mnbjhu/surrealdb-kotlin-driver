package uk.gibby.driver.rpc

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.gibby.driver.Surreal

/**
 * Use
 *
 * This method sets the namespace and database for the current connection
 *
 * @param ns The namespace to use
 * @param db The database to use
 */
suspend fun Surreal.use(ns: String, db: String) {
    sendRequest("use", buildJsonArray { add(ns); add(db) })
}

