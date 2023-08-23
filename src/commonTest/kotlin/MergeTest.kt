import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.query.bind
import uk.gibby.driver.rpc.create
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.update
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class MergeTest {

    @Test
    fun testBasicMerge() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
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
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        val result = connection.update("test", "123")
            .merge<TestClass>(
                bind("myText", "updated"),
            )
        assertEquals("updated", result.myText)
        assertEquals(1, result.myNumber)
    }

    @Test
    fun testMergeWithThing() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val thing = connection.create("test").content(TestClass("first", 1))
        val result = connection.update(thing.id)
            .merge<TestClass>(
                bind("myText", "updated"),
            )
        assertEquals("updated", result.myText)
        assertEquals(1, result.myNumber)
    }

    @Test
    fun testMergeJson() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test")
            .merge<TestClass>(buildJsonObject {
                put("myText", "updated")
                put("myNumber", 2)
            })
        assertEquals(2, result.size)
        result.forEach {
            assertEquals("updated", it.myText)
            assertEquals(2, it.myNumber)
        }
    }

    @Test
    fun testMergeJsonWithThing() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val thing = connection.create("test").content(TestClass("first", 1))
        val result = connection.update(thing.id)
            .merge<TestClass>(buildJsonObject {
                put("myText", "updated")
                put("myNumber", 2)
            })
        assertEquals("updated", result.myText)
        assertEquals(2, result.myNumber)
    }
}