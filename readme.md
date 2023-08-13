# SurrealDB Kotlin Driver
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver)
[![Java CI with Gradle](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml/badge.svg)](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml)
## Overview
A simple Kotlin Multiplatform driver for SurrealDB.

## Usage
### Dependency
build.gradle.kts
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("uk.gibby:surrealdb-kotlin-driver:$kotlin_driver_version")
}
```
build.gradle
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

Reading records
```kotlin
// Select a record by ID
val record = db.select<User>("user", "123")
assert(record.username == "John")
assert(record.password == "1234")

// Select all records
val records = db.select<User>("user")
assert(records.size == 1)
assert(records[0].username == "John")
assert(records[0].password == "1234")
```

Updating records
```kotlin
// Update a record by ID
db.update("user", "123").content(User("John Updated", "1234"))



db.update("user", "123").merge(
    bind(username, "John Updated"),
)

db.update("John", "123").patch { 
    
}
```
```



