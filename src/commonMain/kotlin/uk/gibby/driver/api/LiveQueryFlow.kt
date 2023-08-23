package uk.gibby.driver.api

import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.gibby.driver.Surreal
import uk.gibby.driver.annotation.SurrealDbNightlyOnlyApi
import uk.gibby.driver.model.rpc.LiveQueryAction

/**
 * Live query flow
 *
 * This class is a wrapper around a flow of [LiveQueryAction]s making it [Closeable].
 *
 * @param T The type of the records
 * @property flow The flow of [LiveQueryAction]s
 * @property id The id of the live query
 * @property connection The connection to the database
 * @constructor Creates a new live query flow
 */
@SurrealDbNightlyOnlyApi
class LiveQueryFlow<T>(
    private val flow: Flow<LiveQueryAction<T>>,
    val id: String,
    private val connection: Surreal
): Flow<LiveQueryAction<T>> by flow, Closeable {

    /**
     * Close
     *
     * This method will unsubscribe from the live query and trigger the kill RPC request.
     */

    override fun close() {
        connection.unsubscribe(id)
        connection.triggerKill(id)
    }

    /**
     * Map
     *
     * Used to map the records of the live query flow form [T] to [R].
     *
     * @param R The type of the records in the new flow
     * @param transform The transform function from [T] to [R]
     * @receiver The live query flow to map
     * @return A new live query flow of [R]s
     */
    fun <R>map(transform: (LiveQueryAction<T>) -> LiveQueryAction<R>): LiveQueryFlow<R> {
        return LiveQueryFlow(
            flow = flow.map { transform(it) },
            id = id,
            connection = connection
        )
    }
}