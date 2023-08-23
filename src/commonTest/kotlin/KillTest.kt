import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.annotation.SurrealDbNightlyOnlyApi
import uk.gibby.driver.rpc.kill
import uk.gibby.driver.rpc.live
import uk.gibby.driver.rpc.signin
import uk.gibby.driver.rpc.use
import utils.cleanDatabase
import kotlin.test.Ignore
import kotlin.test.Test

class KillTest {
    @OptIn(SurrealDbNightlyOnlyApi::class)
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