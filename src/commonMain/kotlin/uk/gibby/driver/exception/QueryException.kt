package uk.gibby.driver.exception

data class QueryException(val detail: String): Exception("SurrealDB responded with an error: '$detail'")