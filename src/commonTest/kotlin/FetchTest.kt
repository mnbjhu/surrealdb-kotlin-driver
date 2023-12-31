import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.Thing
import uk.gibby.driver.model.query.data
import uk.gibby.driver.rpc.create
import uk.gibby.driver.rpc.query
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
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
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.query("CREATE other:123 SET linked = test:123;")
        val result = connection.query("SELECT * FROM other FETCH linked;")
        val other = result
            .first()
            .data<List<OtherTest>>()
            .first()
        assertTrue { other.linked is Thing.Record }
        val linked = other.linked as Thing.Record<TestClass>
        assertEquals("first", linked.result.myText)
        assertEquals(1, linked.result.myNumber)

    }

    @Test
    fun testSelectReference() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.create("test", "123").content(TestClass("first", 1))
        connection.query("CREATE other:123 SET linked = test:123;")
        val result = connection.query("SELECT * FROM other;")
        val other = result
            .first()
            .data<List<OtherTest>>()
            .first()
        assertTrue { other.linked is Thing.Reference }
        val linked = other.linked as Thing.Reference<TestClass>
        assertEquals("test:123", linked.id)
    }
}