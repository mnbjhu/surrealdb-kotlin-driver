package uk.gibby.driver.rpc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.rpc.exception.QueryException
import uk.gibby.driver.surrealJson

@Serializable
sealed class QueryResponse {
    abstract val time: String
    @Serializable
    @SerialName("OK")
    data class Success(override val time: String, val result: JsonElement): QueryResponse()

    @Serializable
    @SerialName("ERR")
    data class Error(override val time: String, val detail: String): QueryResponse()
}

inline fun <reified T> QueryResponse.result(): T {
    return when (this) {
        is QueryResponse.Success -> surrealJson.decodeFromJsonElement(result)
        is QueryResponse.Error -> throw QueryException(detail)
    }
}