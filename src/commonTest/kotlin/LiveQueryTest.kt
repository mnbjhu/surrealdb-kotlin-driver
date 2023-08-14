import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.bind
import utils.cleanDatabase
import kotlin.test.Test

class LiveQueryTest {
    @Test
    fun testLiveQuery() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val incoming = connection.live("test")
        connection.create("test").content(buildJsonObject { put("thing", "thing") })
        println(incoming.first())
    }
}