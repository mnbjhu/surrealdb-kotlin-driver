import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.Thing
import uk.gibby.driver.model.query.data
import uk.gibby.driver.model.unknown
import uk.gibby.driver.rpc.create
import uk.gibby.driver.rpc.query
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
class TestClass(
    val myText: String,
    val myNumber: Int,
    val id: Thing<TestClass> = unknown(),
) {
    override fun equals(other: Any?): Boolean {
        if (other !is TestClass) return false
        val idCheck = (id.id == other.id.id || id.id == "unknown" || other.id.id == "unknown")
        return myText == other.myText && myNumber == other.myNumber && idCheck
    }

    override fun hashCode(): Int {
        var result = myText.hashCode()
        result = 31 * result + myNumber
        result = 31 * result + id.hashCode()
        return result
    }
}

class CreateTest {

    @Test
    fun testBasicCreate() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
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
        connection.signin("root", "root")
        connection.use("test", "test")
        val response = connection.create("test", "123").content(TestClass("test", 1))
        assertEquals("test", response.myText)
        assertEquals(1, response.myNumber)
    }

    @Test
    fun testCreateWithUnknownId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val response = connection.create("user", "123").content(User(username = "test", password = "test"))
        assertEquals("test", response.username)
        assertEquals("test", response.password)
        assertEquals("user:123", response.id.id)
    }

    @Test
    fun testCreateWithSelectedId() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val created = connection.create("test").content(TestClass("test", 1))
        connection.create("other").content(OtherTest(created.id))
        val result = connection.query("SELECT * FROM other FETCH linked;")
        val other = result
            .first()
            .data<List<OtherTest>>()
            .first()
        assertTrue { other.linked is Thing.Record }
        val linked = other.linked as Thing.Record<TestClass>
        assertEquals("test", linked.result.myText)
        assertEquals(1, linked.result.myNumber)

    }
}