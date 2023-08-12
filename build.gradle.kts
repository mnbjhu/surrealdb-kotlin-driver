plugins {
    kotlin("multiplatform") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("com.vanniktech.maven.publish") version "0.25.3"
    signing
}

group = "uk.gibby"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    google()
}

buildscript {
    dependencies{
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.25.2")
    }
}

kotlin {
    jvm()

    ios {
        binaries {
            framework("SurrealDB")
        }
    }

    macosX64()
    mingwX64()
    linuxX64()
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.ktor:ktor-client-websockets:2.2.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.2")
                implementation("io.ktor:ktor-client-core:2.2.2")
                implementation("io.ktor:ktor-client-encoding:2.2.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-client-cio:2.2.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.2.2")
            }
        }
        val iosTest by getting {
        }
        val macosX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.2.2")
            }
        }
        val macosX64Test by getting {
        }
        val mingwX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:2.2.2")
            }
        }
        val mingwX64Test by getting {
        }
        val linuxX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.2.2")
            }
        }
        val linuxX64Test by getting {
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

publishing {

}