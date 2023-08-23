package uk.gibby.driver.api

import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.gibby.driver.Surreal
import uk.gibby.driver.model.rpc.LiveQueryAction

class LiveQueryFlow<T>(
    private val flow: Flow<LiveQueryAction<T>>,
    val id: String,
    private val connection: Surreal
): Flow<LiveQueryAction<T>> by flow, Closeable {
    override fun close() {
        connection.unsubscribe(id)
        connection.triggerKill(id)
    }

    fun <R>map(transform: (LiveQueryAction<T>) -> LiveQueryAction<R>): LiveQueryFlow<R> {
        return LiveQueryFlow(
            flow = flow.map { transform(it) },
            id = id,
            connection = connection
        )
    }
}