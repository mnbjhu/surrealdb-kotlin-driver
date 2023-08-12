import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.Thing
import uk.gibby.driver.rpc.model.result
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
data class OtherTest(val linked: Thing<TestClass>)

class FetchTest {

    @Test
    fun testFetchLinkedField() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.query("CREATE other:123 SET linked = test:123;")
        val result = connection.query("SELECT * FROM other FETCH linked;")
        val other = result
            .first()
            .result<List<OtherTest>>()
            .first()
        assertTrue { other.linked is Thing.Actual }
        val linked = other.linked as Thing.Actual<TestClass>
        assertEquals("first", linked.result.myText)
        assertEquals(1, linked.result.myNumber)

    }

    @Test
    fun testSelectReference() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signIn("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.query("CREATE other:123 SET linked = test:123;")
        val result = connection.query("SELECT * FROM other;")
        val other = result
            .first()
            .result<List<OtherTest>>()
            .first()
        assertTrue { other.linked is Thing.Reference }
        val linked = other.linked as Thing.Reference<TestClass>
        assertEquals("test:123", linked.id)
    }
}