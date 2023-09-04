@file:Suppress("VulnerableLibrariesLocal", "SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "studios.pinkcloud.celestial"
version = "1.0-SNAPSHOT"
val versionNotNull: String = version.toString()

val jvmTarget = 17

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://m2.dv8tion.net/releases")
    maven ("https://jitpack.io")
}

val jdaVersion: String by project
val kamlVersion: String by project
val ktomlVersion: String by project
val slf4jVersion: String by project
val coroutinesVersion: String by project
val gsonVersion: String by project
val botCommandsVersion: String by project
val mongodbVersion: String by project
val redisVersion: String by project
val loggingVersion: String by project
val ktorVersion: String by project

dependencies {
    // Java Discord Api Wrapper
    implementation("net.dv8tion:JDA:$jdaVersion")
    // Kotlin Yaml Implementation
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")
    // Logger
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    // Kotlin Toml Implemetation
    implementation("com.akuleshov7:ktoml-source-jvm:$ktomlVersion")
    implementation("com.akuleshov7:ktoml-core:$ktomlVersion")
    implementation("com.akuleshov7:ktoml-file:$ktomlVersion")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    // Kotlin Json Implementation
    implementation("com.google.code.gson:gson:$gsonVersion")
    // JDA Command Wrapper
    implementation("io.github.freya022:BotCommands:$botCommandsVersion")
    // Kotlin MongoDB Driver
    implementation("org.mongodb:mongodb-driver-sync:$mongodbVersion")
    // Kotlin Redis
    implementation("redis.clients:jedis:$redisVersion")
    // Extra Logger
    implementation("io.github.microutils:kotlin-logging:$loggingVersion")
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    exclude("**/*.kotlin_metadata")
    exclude("**/*.kotlin_builtins")
    exclude("META-INF/")

    archiveFileName.set("Celestial-$versionNotNull.jar")
}

kotlin {
    jvmToolchain(jvmTarget)
}

application {
    mainClass.set("$group.MainKt")
}