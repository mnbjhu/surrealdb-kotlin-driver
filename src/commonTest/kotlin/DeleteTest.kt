import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.*
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteTest {

    @Test
    fun testBasicDelete() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        connection.delete("test")
        val response = connection.select<TestClass>("test")
        assertEquals(0, response.size)
    }

    @Test
    fun testDeleteId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.delete("test", "123")
        val result = connection.select<TestClass>("test")
        assertEquals(0, result.size)
    }
}