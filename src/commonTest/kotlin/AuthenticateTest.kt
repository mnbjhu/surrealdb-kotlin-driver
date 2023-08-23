import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.authenticate
import uk.gibby.driver.rpc.info
import uk.gibby.driver.rpc.signup
import uk.gibby.driver.model.query.bind
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticateTest {

    @Test
    fun testBasicAuthenticate() = runTest {
        cleanDatabase()
        SignUpTest.configureTestScope()
        val connection = Surreal("localhost")
        connection.connect()
        val token = connection.signup("test", "test", "test_scope", bind("username", "test"), bind("password", "test"))
        val secondConnection = Surreal("localhost")
        secondConnection.connect()
        secondConnection.authenticate(token)
        val result = secondConnection.info<User>()
        assertEquals("test", result.username)
        assertEquals("test", result.password)
    }
}