package uk.gibby.driver

import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.collections.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.*
import uk.gibby.driver.rpc.exception.LiveQueryKilledException
import uk.gibby.driver.rpc.functions.kill
import uk.gibby.driver.rpc.model.*

/**
 * SurrealDB driver
 *
 * The entry point for connection to SurrealDB.
 *
 * @param host SurrealDB host (e.g. `localhost`)
 * @param port SurrealDB port
 */
class Surreal(private val host: String, private val port: Int = 8000) {
    private var count = 0L
    private var connection: DefaultClientWebSocketSession? = null
    private val requests = ConcurrentMap<String, Channel<JsonElement>>()
    private val liveQueries = ConcurrentMap<String, Channel<LiveQueryAction<JsonElement>>>()
    private val context = CoroutineScope(Dispatchers.Default)

    /**
     * Connects to SurrealDB and removes the existing connection if one exists.
     */
    suspend fun connect() {
        connection?.cancel()
        connection = Client.webSocketSession(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/rpc",
        ).also {
            context.launch {
                it.incoming.receiveAsFlow().collect {
                    it as Frame.Text
                    val response = try {
                        surrealJson.decodeFromString(RpcResponseSerializer, it.readText())
                    } catch (e: Exception) {
                        // In theory this could be an error for any request, so we cancel all of them
                        requests.forEach { (_, r) ->  r.cancel(CancellationException("Failed to decode incoming response: ${it.readText()}\n${e.message}"))}
                        throw e
                    }
                    when(response) {
                        is RpcResponse.Success -> handleSuccess(response)
                        is RpcResponse.Error -> handleError(response)
                        is RpcResponse.Notification -> handleNotification(response)
                    }
                }
            }
        }
    }

    private suspend fun handleSuccess(response: RpcResponse.Success) {
        val request = requests[response.id]
        if (request != null) {
            request.send(response.result)
        } else {
            requests.forEach {
                // In theory this could be an error for any request, so we cancel all of them
                    (_, r) ->
                r.cancel(CancellationException("Received a request with an unknown id: ${response.id} body: $response"))
            }
        }
    }

    private fun handleError(response: RpcResponse.Error) {
        val request = requests[response.id]
        if (request != null) {
            request.cancel(CancellationException("SurrealDB responded with an error: '${response.error}'"))
        } else {
            requests.forEach {
                // In theory this could be an error for any request, so we cancel all of them
                    (_, r) ->
                r.cancel(CancellationException("Received a request with an unknown id: ${response.id} body: $response"))
            }
        }
        requests.remove(response.id)
    }

    private suspend fun handleNotification(response: RpcResponse.Notification) {
        val action = response.result
        val liveQuery = liveQueries.getOrPut(action.id) { Channel() }
        context.launch { liveQuery.send(response.result) }
    }

    internal suspend fun sendRequest(method: String, params: JsonArray): JsonElement {
        val id = count++.toString()
        val request = RpcRequest(id, method, params)
        val channel = Channel<JsonElement>(1)
        requests[id] = channel
        (connection ?: throw Exception("SurrealDB: Websocket not connected")).sendSerialized(request)
        return channel.receive()
    }

    fun subscribeAsJson(liveQueryId: String): Flow<LiveQueryAction<JsonElement>> {
        val channel = liveQueries.getOrPut(liveQueryId) { Channel() }
        return channel.receiveAsFlow()
    }

    inline fun <reified T> subscribe(liveQueryId: String): Flow<LiveQueryAction<T>> {
        return subscribeAsJson(liveQueryId).map { it.asType() }
    }

    fun unsubscribe(liveQueryId: String) {
        val channel = liveQueries[liveQueryId]
        channel?.cancel(LiveQueryKilledException)
        liveQueries.remove(liveQueryId)
    }

    internal fun triggerKill(liveQueryId: String) {
        context.launch { kill(liveQueryId) }
    }

}

