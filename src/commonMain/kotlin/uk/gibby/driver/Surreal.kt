package uk.gibby.driver

import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.collections.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.*
import uk.gibby.driver.rpc.model.RpcRequest
import uk.gibby.driver.rpc.model.RpcResponse
import uk.gibby.driver.rpc.model.RpcResponseSerializer
import kotlinx.serialization.encodeToString

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
                        requests.forEach { (_, r) ->  r.cancel(CancellationException("Failed to decode incoming response: ${it.readText()}\n${e.message}"))}
                        throw e
                    }
                    val request = requests[response.id]
                    if (request == null) requests.forEach { (_, r) ->  r.cancel(CancellationException("Received a request with an unknown id: ${response.id} body: $response"))}
                    else when(response) {
                        is RpcResponse.Success -> request.send(response.result)
                        is RpcResponse.Error -> request.cancel(CancellationException("SurrealDB responded with an error: '${response.error}'"))
                    }
                    requests.remove(response.id)
                }
            }
        }
    }

    internal suspend fun sendRequest(method: String, params: JsonArray): JsonElement {
        val id = count++.toString()
        val request = RpcRequest(id, method, params)
        val channel = Channel<JsonElement>(1)
        requests[id] = channel
        (connection ?: throw Exception("SurrealDB: Websocket not connected")).sendSerialized(request)
        return channel.receive()
    }

}