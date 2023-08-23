import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.let
import uk.gibby.driver.rpc.query
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
import uk.gibby.driver.model.query.data
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class LetTest {

    @Test
    fun testLet() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.let("myKey", "myValue")
        val result = connection.query("RETURN \$myKey;")
        assertEquals("myValue", result.first().data())
    }
}