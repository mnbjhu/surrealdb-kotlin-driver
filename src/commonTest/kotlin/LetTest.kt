import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.let
import uk.gibby.driver.rpc.functions.query
import uk.gibby.driver.rpc.functions.signin
import uk.gibby.driver.rpc.functions.use
import uk.gibby.driver.rpc.model.result
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
        assertEquals("myValue", result.first().result())
    }
}