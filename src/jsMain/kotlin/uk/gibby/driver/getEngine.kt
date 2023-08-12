package uk.gibby.driver

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun getEngine(): HttpClientEngineFactory<*> {
    return Js
}