package utils

import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.invalidate
import uk.gibby.driver.rpc.query
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use

suspend fun cleanDatabase() {
    val connection = Surreal("localhost")
    connection.connect()
    connection.signin("root", "root")
    connection.use("test", "test")
    connection.query("REMOVE SCOPE test_scope;")
    connection.query("REMOVE DATABASE test;")
    connection.query("DEFINE DATABASE test;")
    connection.invalidate()
}