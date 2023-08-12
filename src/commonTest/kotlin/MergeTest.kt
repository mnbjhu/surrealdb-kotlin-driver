import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.bind
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class MergeTest {

    @Test
    fun testBasicMerge() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test")
            .merge<TestClass>(
                bind("myText", "updated"),
            )
        assertEquals(2, result.size)
        result.forEach {
            assertEquals("updated", it.myText)
        }
    }

    @Test
    fun testMergeWithId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        val result = connection.update("test", "123")
            .merge<TestClass>(
                bind("myText", "updated"),
            )
        assertEquals("updated", result.myText)
        assertEquals(1, result.myNumber)
    }
}