import kotlinx.coroutines.test.runTest
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.functions.*
import uk.gibby.driver.rpc.model.bind
import utils.cleanDatabase
import kotlin.jvm.JvmStatic
import kotlin.test.Test

class SignUpTest {

    @Test
    fun testSignUp() = runTest {
        val connection = Surreal("localhost")
        cleanDatabase()
        configureTestScope()
        connection.connect()
        connection.signup(
            "test",
            "test",
            "test_scope",
            bind("username", "test_user"),
            bind("password", "test_password")
        )
    }

    companion object {
        @JvmStatic
        suspend fun configureTestScope() {

            val connection = Surreal("localhost")
            connection.connect()
            connection.signin("root", "root")
            connection.use("test", "test")
            connection.query(
                "DEFINE TABLE user;" +
                "DEFINE FIELD username ON user TYPE string;" +
                "DEFINE FIELD password ON user TYPE string;" +
                "DEFINE SCOPE test_scope " +
                "SESSION 2h " +
                "SIGNUP (CREATE user SET username = \$username, password = \$password) " +
                "SIGNIN (SELECT * FROM user WHERE username = \$username AND password = \$password);"
            )
            connection.invalidate()
        }
    }
}