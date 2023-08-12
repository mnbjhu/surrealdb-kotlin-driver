package uk.gibby.driver.rpc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import uk.gibby.driver.surrealJson

@Serializable
data class JsonPatch(
    val op: Operation,
    val path: String,
    val value: JsonElement? = null,
) {

    @Serializable
    enum class Operation {
        @SerialName("add")
        ADD,
        @SerialName("remove")
        REMOVE,
        @SerialName("replace")
        REPLACE,
        @SerialName("move")
        MOVE,
        @SerialName("copy")
        COPY,
        @SerialName("test")
        TEST,
        @SerialName("change")
        CHANGE
    }

    class Builder {
        private val patches = mutableListOf<JsonPatch>()

        fun add(path: String, value: JsonElement) {
            patches.add(JsonPatch(Operation.ADD, path, value))
        }

        inline fun <reified T> add(path: String, value: T) {
            add(path, surrealJson.encodeToJsonElement(value))
        }

        fun remove(path: String) {
            patches.add(JsonPatch(Operation.REMOVE, path))
        }

        fun replace(path: String, value: JsonElement) {
            patches.add(JsonPatch(Operation.REPLACE, path, value))
        }

        inline fun <reified T> replace(path: String, value: T) {
            replace(path, surrealJson.encodeToJsonElement(value))
        }

        fun move(from: String, path: String) {
            patches.add(JsonPatch(Operation.MOVE, path, null))
        }

        fun copy(from: String, path: String) {
            patches.add(JsonPatch(Operation.COPY, path, null))
        }

        fun test(path: String, value: JsonElement) {
            patches.add(JsonPatch(Operation.TEST, path, value))
        }

        inline fun <reified T> test(path: String, value: T) {
            test(path, surrealJson.encodeToJsonElement(value))
        }

        internal fun build(): List<JsonPatch> {
            return patches.toList()
        }
    }
}
