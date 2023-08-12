package uk.gibby.driver.rpc.functions

import kotlinx.serialization.json.JsonArray
import uk.gibby.driver.Surreal

suspend fun Surreal.invalidate(){
    sendRequest("invalidate", JsonArray(listOf()))
}
