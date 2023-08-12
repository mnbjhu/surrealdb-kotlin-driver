import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class PatchTest {

    @Test
    fun testBasicPatch() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test").content(TestClass("first", 1))
        connection.create("test").content(TestClass("second", 2))
        val result = connection.update("test").patch {
            replace("myText", "updated")
        }
        assertEquals(2, result.size)
    }

    @Test
    fun testPatchWithId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.update("test", "123").patch {
            replace("myText", "updated")
        }
        val result = connection.select<TestClass>("test", "123")
        assertEquals("updated", result.myText)
    }
}