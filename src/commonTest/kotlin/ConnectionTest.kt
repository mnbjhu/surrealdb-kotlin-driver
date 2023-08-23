import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.query.bind
import uk.gibby.driver.rpc.*
import kotlin.test.Ignore
import kotlin.test.Test

class ConnectionTest {

    @Test
    fun connectAsRoot() = runTest {
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.invalidate()
    }

    @Test
    fun connectAsUser() = runTest {
        val connection = Surreal("localhost", 8000)
        connection.connect()
        connection.signin("root", "root")
        connection.use("test", "test")
        connection.query("REMOVE SCOPE test_scope;")
        connection.query("" +
            "DEFINE TABLE user;\n" +
            "DEFINE FIELD username ON user TYPE string;\n" +
            "DEFINE FIELD password ON user TYPE string;\n" +
            "DEFINE SCOPE test_scope\n" +
            "SESSION 2h\n" +
            "SIGNUP (CREATE user SET username = \$username, password = \$password)\n" +
            "SIGNIN (SELECT * FROM user WHERE username = \$username AND password = \$password);"
        )
        connection.invalidate()
        connection.signup(
            "test",
            "test",
            "test_scope",
            bind("username", "test_user"),
            bind("password", "test_password")
        )
    }
}