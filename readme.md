# SurrealDB Kotlin Driver
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.gibby/surrealdb-kotlin-driver)
[![Java CI with Gradle](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml/badge.svg)](https://github.com/mnbjhu/surrealdb-kotlin-driver/actions/workflows/gradle.yml)
## Overview
A simple Kotlin Multiplatform driver for SurrealDB.

## Usage
### Dependency
<b>build.gradle.kts</b>
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("uk.gibby:surrealdb-kotlin-driver:$kotlin_driver_version")
}
```
<b>build.gradle</b>
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "uk.gibby:surrealdb-kotlin-driver:$kotlin_driver_version"
}
```

### Example
<b>Connecting to a SurrealDB instance</b>
```kotlin
val db = Surreal("localhost", 8000)
db.connect()
db.signin("root", "root")
// db.signin("ns", "db", "scope", bind("username", "John"), bind("password", "1234"))
db.use("ns", "db")
```

<b>Creating a records</b>
```kotlin
// Create a record from a JSON object
db.create("user").content(buildJsonObject( put("username", "John"), put("password", "1234")))

// Create a record from a @Serializable object
@Serializable
data class User(val username: String, val password: String)

db.create("user").content(User("John", "1234"))
```
Note: All functions have both a JsonObject and @Serializable variant so you can interact with SurrealDB in both a shemafull and schemaless way.

<b>Reading records</b>
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

<b>Updating records</b>
```kotlin
// Update a record by ID
db.update("user", "123").content(User("John Updated", "1234"))

// Update all records
db.update("user").content(User("John Updated", "1234"))

// Update with a merge
db.update("user", "123").merge(
    bind(username, "John Updated"),
)

// Update with a Json patch
db.update("John", "123").patch { 
    // Json patch builder
    replace("username", "John Updated")    
}
```

<b>Deleting records</b>
```kotlin
// Delete a record by ID
db.delete("user", "123")

// Delete all records
db.delete("user")
```

<b>Querying records</b>
```kotlin
val result = db.query(
    "SELECT * FROM user WHERE username = $username AND password = $password\n" +
    "ORDER BY username;",
    bind("username", "John"),
    bind("password", "1234")
)
assert(result.size == 1)
val users = result.first().data<List<User>>()
```

<b>Using Record Links</b>
In order to interact with id's in a type safe way, you can use `Thing` type.

```kotlin

import java.time.ZoneId

@Serializable
data class User(
    val id: Thing<User> = unknown(),
    val username: String,
    val password: String
)

@Serializable
data class Post(
    val id: Thing<Post> = unknown(),
    val author: Thing<User>,
    val content: String,
)

val user = create("user").content(User(username = "John", password = "1234"))
val post = create("post").content(Post(author = user.id, content = "Hello World!"))
```
A `Thing` can be a `Reference` (an id) or a `Record` (a full record). You can use SurrealDB's 'FETCH' statement to fetch a record from a reference.

```kotlin
// By default, a Thing is a reference
val post = select<Post>("post", "123")

assert(post.author is Thing.Reference<User>)
assert(post.author.id == "user:123")

// You can fetch a record from a reference
val queryResult = query(
    "SELECT * FROM post WHERE author = $author\n" +
    "FETCH author LIMIT 1;",
    bind("author", "John")
)
val post = queryResult.first().data<List<Post>>()[0]

assert(post.author is Thing.Record<User>)

post as Thing.Record<User>
val author = post.author.data<User>()

assert(author.username == "John")
assert(author.password == "1234")
```

## Links
- [SurrealDB](https://surrealdb.com/)
- [Api Documentation]()
- [Installing SurrealDB](https://surrealdb.com/docs/installation)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)