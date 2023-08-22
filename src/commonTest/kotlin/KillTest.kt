import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.kill
import uk.gibby.driver.rpc.functions.live
import uk.gibby.driver.rpc.functions.signin
import uk.gibby.driver.rpc.functions.use
import utils.cleanDatabase
import kotlin.test.Test

class KillTest {
    @Test
    fun testKill() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        val liveQueryId = connection.live("test")
        connection.kill(liveQueryId)
    }
}