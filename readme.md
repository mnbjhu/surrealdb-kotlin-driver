# SurrealDB Kotlin Driver
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver)
[![Java CI with Gradle](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml/badge.svg)](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml)
## Overview
A simple Kotlin Multiplatform driver for SurrealDB. This driver is a work in progress and is not yet ready for production use.

## Usage
### Gradle
build.gradle
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("uk.gibby:surrealdb-kotlin-driver:$kotlin_driver_version")
}
```
build.gradle.kts
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "uk.gibby:surrealdb-kotlin-driver:$kotlin_driver_version"
}
```

### Example
Connecting to a SurrealDB instance
```kotlin
val db = Surreal("localhost", 8000)
db.connect()
db.signin("root", "root")
// db.signin("ns", "db", "scope", bind("username", "John"), bind("password", "1234"))
db.use("ns", "db")
```

Creating a records
```kotlin
// Create a record from a JSON object
db.create("user").content(buildJsonObject( put("username", "John"), put("password", "1234")))

// Create a record from a @Serializable object
@Serializable
data class User(val username: String, val password: String)

db.create("user").content(User("John", "1234"))
```




