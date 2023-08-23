import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.create
import uk.gibby.driver.rpc.select
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class SelectTest {

    @Test
    fun testBasicSelect() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val response = connection.select<TestClass>("test")
        assertEquals(2, response.size)
        val first = response.first { it.myText == "first" }
        assertEquals(1, first.myNumber)
        val second = response.first { it.myText == "second" }
        assertEquals(2, second.myNumber)
    }

    @Test
    fun testSelectId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        val result = connection.select<TestClass>("test", "123")
        assertEquals("first", result.myText)
        assertEquals(1, result.myNumber)
    }

    @Test
    fun testSelectThing() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val thing = connection.create("test").content(TestClass("first", 1))
        val result = connection.select<TestClass>(thing.id)
        assertEquals("first", result.myText)
        assertEquals(1, result.myNumber)
    }
}