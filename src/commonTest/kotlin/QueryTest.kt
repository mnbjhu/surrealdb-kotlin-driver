import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.query
import uk.gibby.driver.rpc.functions.signIn
import uk.gibby.driver.rpc.functions.use
import uk.gibby.driver.rpc.model.bind
import uk.gibby.driver.rpc.model.result
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class QueryTest {

    @Test
    fun testBasicQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        val response = connection.query("RETURN 'Success';")
        assertEquals(1, response.size)
        assertEquals("Success", response[0].result())
    }

    @Test
    fun testParameterizedQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        val response = connection.query("RETURN \$test;", bind("test", "test"))
        assertEquals(1, response.size)
        assertEquals("test", response[0].result())
    }

    @Test
    fun testInvalidQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        assertFails {
            connection.query("SOME INVALID QUERY;")
        }
    }
}