import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.api.observeLiveQuery
import uk.gibby.driver.exception.LiveQueryKilledException
import uk.gibby.driver.model.rpc.LiveQueryAction
import uk.gibby.driver.model.query.bind
import uk.gibby.driver.model.query.data
import uk.gibby.driver.rpc.*
import utils.cleanDatabase
import kotlin.test.*

class LiveQueryTest {
    @Test
    fun testObserve() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")

        val incoming = connection.observeLiveQuery<TestClass>("test")
        connection.create("test", "first").content(TestClass("thing", 1))
        connection.create("test", "second").content(TestClass("thing", 2))
        connection.update("test", "first").patch {
            replace("myText", "thing2")
        }
        connection.delete("test", "second")

        val all = List(4) { incoming.first() }
        assertEquals(4, all.size)
        val shouldReceive = listOf(
            LiveQueryAction.Create(incoming.id, TestClass("thing", 1)),
            LiveQueryAction.Create(incoming.id, TestClass("thing", 2)),
            LiveQueryAction.Update(incoming.id, TestClass("thing2", 1)),
            LiveQueryAction.Delete(incoming.id, "test:second")
        )
        shouldReceive.forEach { assertContains(all, it) }

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

        val all = List(4) { incoming.first() }
        assertEquals(4, all.size)
        val shouldReceive = listOf(
            LiveQueryAction.Create(liveQueryId, TestClass("thing", 1)),
            LiveQueryAction.Create(liveQueryId, TestClass("thing", 2)),
            LiveQueryAction.Update(liveQueryId, TestClass("thing2", 1)),
            LiveQueryAction.Delete(liveQueryId, "test:second")
        )
        shouldReceive.forEach { assertContains(all, it) }

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

        val all = List(4) { incoming.first() }
        assertEquals(4, all.size)
        val shouldReceive = listOf(
            LiveQueryAction.Create(liveQueryId, TestClass("thing", 1)),
            LiveQueryAction.Create(liveQueryId, TestClass("thing", 2)),
            LiveQueryAction.Update(liveQueryId, TestClass("thing2", 1)),
            LiveQueryAction.Delete(liveQueryId, "test:second")
        )
        shouldReceive.forEach { assertContains(all, it) }

        connection.kill(liveQueryId)
        connection.unsubscribe(liveQueryId)
        assertFailsWith<LiveQueryKilledException> {
            incoming.first()
        }
    }
}