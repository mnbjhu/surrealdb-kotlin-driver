package uk.gibby.driver.model.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import uk.gibby.driver.model.rpc.LiveQueryAction.*
import uk.gibby.driver.surrealJson


/**
 * LiveQueryAction
 *
 * A live query action. Can be a [Create], [Update] or [Delete].
 * Holds the id of the record and the result if relevant.
 *
 * @param T The type of the result
 */
@Serializable(with = LiveQueryActionSerializer::class)
sealed class LiveQueryAction<out T> {
    abstract val id: String
    data class Create<T>(override val id: String, val result: T): LiveQueryAction<T>()
    data class Update<T>(override val id: String, val result: T): LiveQueryAction<T>()
    data class Delete(override val id: String, val deletedId: String): LiveQueryAction<Nothing>()
}


/**
 * As type
 *
 * Maps the live query to type [T] using [decodeFromJsonElement]
 *
 * @param T The new type
 * @return The new live query action
 */
inline fun <reified T> LiveQueryAction<JsonElement>.asType(): LiveQueryAction<T> {
    return when(this) {
        is Delete -> this
        is Create -> Create(id, surrealJson.decodeFromJsonElement(result))
        is Update -> Update(id, surrealJson.decodeFromJsonElement(result))
    }
}

