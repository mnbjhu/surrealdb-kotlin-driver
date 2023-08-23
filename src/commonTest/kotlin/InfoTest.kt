import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import uk.gibby.driver.Surreal
import uk.gibby.driver.rpc.info
import uk.gibby.driver.rpc.signup
import uk.gibby.driver.model.Thing
import uk.gibby.driver.model.query.bind
import uk.gibby.driver.model.unknown
import utils.cleanDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class User(
    val id: Thing<User> = unknown(),
    val username: String,
    val password: String
)

class InfoTest {

    @Test
    fun testInfo() = runTest {
        cleanDatabase()
        SignUpTest.configureTestScope()
        val connection = Surreal("localhost")
        connection.connect()
        connection.signup("test", "test", "test_scope", bind("username", "test_user"), bind("password", "test_password"))
        val user = connection.info<User>()
        assertEquals("test_user", user.username)
        assertEquals("test_password", user.password)
    }
}