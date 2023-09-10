import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.insert
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertTest {

    @Test
    fun basicInsertTest() = runTest {
        cleanDatabase()
        val db = Surreal("localhost", 8000)
        db.connect()
        db.signin("root", "root")
        db.use("test", "test")
        val created = db.insert("test").content(
            TestClass("first", 1),
            TestClass("second", 2),
            TestClass("third", 3)
        )
        assertEquals(3, created.size)
        assertEquals("first", created[0].myText)
        assertEquals(1, created[0].myNumber)
        assertEquals("second", created[1].myText)
        assertEquals(2, created[1].myNumber)
        assertEquals("third", created[2].myText)
        assertEquals(3, created[2].myNumber)
    }
}