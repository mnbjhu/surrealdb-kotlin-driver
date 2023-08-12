import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.result
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class UnsetTest {

    @Test
    fun testUnset() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.let("myKey", "myValue")
        val firstResult = connection.query("RETURN \$myKey;")
        val myKey = firstResult.first().result<String?>()
        assertEquals("myValue", myKey)
        connection.unset("myKey")
        val secondResult = connection.query("RETURN \$myKey;")
        val myKeyDeleted = secondResult.first().result<String?>()
        assertEquals(null, myKeyDeleted)
    }
}