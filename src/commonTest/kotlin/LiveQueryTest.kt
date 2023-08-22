import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.exception.LiveQueryKilledException
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.LiveQueryAction
import uk.gibby.driver.rpc.model.bind
import uk.gibby.driver.rpc.model.data
import utils.cleanDatabase
import kotlin.test.*

class LiveQueryTest {
    @Test
    fun testLiveQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")

        val incoming = connection.observe<TestClass>("test")
        connection.create("test", "first").content(TestClass("thing", 1))
        connection.create("test", "second").content(TestClass("thing", 2))
        connection.update("test", "first").patch {
            replace("myText", "thing2")
        }
        connection.delete("test", "second")

        val first = incoming.first()
        assertTrue { first is LiveQueryAction.Create }
        first as LiveQueryAction.Create
        assertEquals("thing", first.result.myText)
        assertEquals(1, first.result.myNumber)

        val second = incoming.first()
        assertTrue { second is LiveQueryAction.Create }
        second as LiveQueryAction.Create
        assertEquals("thing", second.result.myText)
        assertEquals(2, second.result.myNumber)

        val updated = incoming.first()
        assertTrue { updated is LiveQueryAction.Update }
        updated as LiveQueryAction.Update
        assertEquals("thing2", updated.result.myText)
        assertEquals(1, updated.result.myNumber)

        val deleted = incoming.first()
        assertTrue { deleted is LiveQueryAction.Delete }
        deleted as LiveQueryAction.Delete
        assertEquals("test:second", deleted.deletedId)

        incoming.close()
        assertFailsWith<LiveQueryKilledException> {
            incoming.first()
        }
    }

    @Test
    fun testLiveQueryAsPartOfRegularQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val result = connection.query("LIVE SELECT * FROM test;")
        val liveQueryId = result.first().data<String>()
        val incoming = connection.subscribe<TestClass>(liveQueryId)

        connection.create("test", "first").content(TestClass("thing", 1))
        connection.create("test", "second").content(TestClass("thing", 2))
        connection.update("test", "first").patch {
            replace("myText", "thing2")
        }
        connection.delete("test", "second")

        val first = incoming.first()
        assertTrue { first is LiveQueryAction.Create }
        first as LiveQueryAction.Create
        assertEquals("thing", first.result.myText)
        assertEquals(1, first.result.myNumber)

        val second = incoming.first()
        assertTrue { second is LiveQueryAction.Create }
        second as LiveQueryAction.Create
        assertEquals("thing", second.result.myText)
        assertEquals(2, second.result.myNumber)

        val updated = incoming.first()
        assertTrue { updated is LiveQueryAction.Update }
        updated as LiveQueryAction.Update
        assertEquals("thing2", updated.result.myText)
        assertEquals(1, updated.result.myNumber)

        val deleted = incoming.first()
        assertTrue { deleted is LiveQueryAction.Delete }
        deleted as LiveQueryAction.Delete
        assertEquals("test:second", deleted.deletedId)

        connection.query("KILL \$liveQueryId;", bind("liveQueryId", liveQueryId))
        connection.unsubscribe(liveQueryId)
        assertFailsWith<LiveQueryKilledException> {
            incoming.first()
        }
    }

    @Test
    fun testLiveQueryUsingRpcFunctions() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val liveQueryId = connection.live("test")
        val incoming = connection.subscribe<TestClass>(liveQueryId)

        connection.create("test", "first").content(TestClass("thing", 1))
        connection.create("test", "second").content(TestClass("thing", 2))
        connection.update("test", "first").patch {
            replace("myText", "thing2")
        }
        connection.delete("test", "second")

        val first = incoming.first()
        assertTrue { first is LiveQueryAction.Create }
        first as LiveQueryAction.Create
        assertEquals("thing", first.result.myText)
        assertEquals(1, first.result.myNumber)

        val second = incoming.first()
        assertTrue { second is LiveQueryAction.Create }
        second as LiveQueryAction.Create
        assertEquals("thing", second.result.myText)
        assertEquals(2, second.result.myNumber)

        val updated = incoming.first()
        assertTrue { updated is LiveQueryAction.Update }
        updated as LiveQueryAction.Update
        assertEquals("thing2", updated.result.myText)
        assertEquals(1, updated.result.myNumber)

        val deleted = incoming.first()
        assertTrue { deleted is LiveQueryAction.Delete }
        deleted as LiveQueryAction.Delete
        assertEquals("test:second", deleted.deletedId)

        connection.kill(liveQueryId)
        connection.unsubscribe(liveQueryId)
        assertFailsWith<LiveQueryKilledException> {
            incoming.first()
        }
    }

}