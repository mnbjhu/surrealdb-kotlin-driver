import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.bind
import utils.cleanDatabase
import kotlin.test.Test

class SignInTest {

    @Test
    fun testSignIn() = runTest {
        cleanDatabase()
        val connection = Surreal("localhost")
        connection.connect()
        SignUpTest.configureTestScope()
        connection.signup("test", "test", "test_scope", bind("username", "test_user"), bind("password", "test_password"))
        connection.invalidate()
        connection.signIn("test", "test", "test_scope", bind("username", "test_user"), bind("password", "test_password"))
    }
}

