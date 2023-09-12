@file:Suppress("SpellCheckingInspection")

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

repositories {
    mavenCentral()
}

val jvmTarget = 17

val ktorVersion: String by project
val jsonSerializationVersion: String by project
val kamlVersion: String by project
val ktomlVersion: String by project
val slf4jVersion: String by project
val mongodbVersion: String by project
val redisVersion: String by project
val redissonVersion: String by project
val okHttpVersion: String by project
val okioVersion: String by project

dependencies {
    // Json Implementation
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$jsonSerializationVersion")
    // Yaml Implementation
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")
    // Toml Implementation
    implementation("com.akuleshov7:ktoml-core:$ktomlVersion")
    implementation("com.akuleshov7:ktoml-file:$ktomlVersion")
    implementation("com.akuleshov7:ktoml-source:$ktomlVersion")
    // Ktor Dependencies
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    // Logging
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    // Database
    implementation("org.mongodb:mongodb-driver-sync:$mongodbVersion")
    implementation("redis.clients:jedis:$redisVersion")
    implementation("org.redisson:redisson:$redissonVersion")
    // Http Client
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    // File IO
    implementation("com.squareup.okio:okio:$okioVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")
}

kotlin {
    jvmToolchain(jvmTarget)
}

application {
    mainClass.set("studios.pinkcloud.celestial.api.AppKt")
}
