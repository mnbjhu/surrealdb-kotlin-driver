package uk.gibby.driver.annotation

@RequiresOptIn(message = "This function is only available while using nightly builds of SurrealDB. See https://surrealdb.com/docs/installation/nightly.")
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SurrealDbNightlyOnlyApi
