import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.query.data
import uk.gibby.driver.rpc.*
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
        val myKey = firstResult.first().data<String?>()
        assertEquals("myValue", myKey)
        connection.unset("myKey")
        val secondResult = connection.query("RETURN \$myKey;")
        val myKeyDeleted = secondResult.first().data<String?>()
        assertEquals(null, myKeyDeleted)
    }
}