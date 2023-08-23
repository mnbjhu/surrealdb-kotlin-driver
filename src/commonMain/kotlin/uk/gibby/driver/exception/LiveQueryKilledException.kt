package uk.gibby.driver.exception

import kotlinx.coroutines.CancellationException

object LiveQueryKilledException: CancellationException("Live query has been killed")