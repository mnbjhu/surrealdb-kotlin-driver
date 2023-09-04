import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.*
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class PatchTest {

    @Test
    fun testBasicPatchReturningDiff() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test").patchWithDiff {
            replace("myText", "updated")
        }
        assertEquals(2, result.size)
    }

    @Test
    fun testPatchWithIdReturningDiff() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.update("test", "123").patchWithDiff {
            replace("myText", "updated")
        }
        val result = connection.select<TestClass>("test", "123")
        assertEquals("updated", result.myText)
    }

    @Test
    fun testPatchWithThingReturningDiff() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val thing = connection.create("test").content(TestClass("first", 1))
        connection.update(thing.id).patchWithDiff {
            replace("myText", "updated")
        }
        val result = connection.select(thing.id)
        assertEquals("updated", result.myText)
    }

    @Test
    fun testPatchWithIdReturningRecords() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.update("test", "123").patch<TestClass> {
            replace("myText", "updated")
        }
        val result = connection.select<TestClass>("test", "123")
        assertEquals("updated", result.myText)
        assertEquals(1, result.myNumber)
    }

    @Test
    fun testPatchWithThingReturningRecords() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val thing = connection.create("test").content(TestClass("first", 1))
        connection.update(thing.id).patch<TestClass> {
            replace("myText", "updated")
        }
        val result = connection.select(thing.id)
        assertEquals("updated", result.myText)
        assertEquals(1, result.myNumber)
    }

    @Test
    fun testPatchReturningRecords() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test").patch<TestClass> {
            replace("myText", "updated")
        }.sortedBy { it.myNumber }
        assertEquals(2, result.size)
        assertEquals("updated", result[0].myText)
        assertEquals(1, result[0].myNumber)
        assertEquals("updated", result[1].myText)
        assertEquals(2, result[1].myNumber)
    }
}