package uk.gibby.driver.rpc.exception

import kotlinx.coroutines.CancellationException

object LiveQueryKilledException: CancellationException("Live query has been killed")