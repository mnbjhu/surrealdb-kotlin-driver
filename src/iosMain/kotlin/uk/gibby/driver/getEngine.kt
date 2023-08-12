package uk.gibby.driver

import io.ktor.client.engine.*

actual fun getEngine(): HttpClientEngineFactory<*> {
    return io.ktor.client.engine.darwin.Darwin
}