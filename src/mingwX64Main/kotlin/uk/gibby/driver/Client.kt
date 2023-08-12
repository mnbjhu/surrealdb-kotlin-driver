package uk.gibby.driver

import io.ktor.client.engine.*
import io.ktor.client.engine.winhttp.*

actual fun getEngine(): HttpClientEngineFactory<*> {
    return WinHttp
}
