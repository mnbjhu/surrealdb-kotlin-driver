import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.create
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.update
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateTest {

    @Test
    fun testBasicUpdate() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test").content(TestClass("updated", -1))
        assertEquals(2, result.size)
        result.forEach {
            assertEquals("updated", it.myText)
            assertEquals(-1, it.myNumber)
        }
    }

    @Test
    fun testUpdateId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        val result = connection.update("test", "123").content(TestClass("updated", -1))
        assertEquals("updated", result.myText)
        assertEquals(-1, result.myNumber)
    }

}