import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.content
import uk.gibby.driver.rpc.functions.create
import uk.gibby.driver.rpc.functions.signIn
import uk.gibby.driver.rpc.functions.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class TestClass(val myText: String, val myNumber: Int)

class CreateTest {

    @Test
    fun testBasicCreate() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        val response = connection.create("test").content(TestClass("test", 1))
        assertEquals("test", response.myText)
        assertEquals(1, response.myNumber)
    }

    @Test
    fun testCreateWithId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        val response = connection.create("test", "123").content(TestClass("test", 1))
        assertEquals("test", response.myText)
        assertEquals(1, response.myNumber)
    }
}