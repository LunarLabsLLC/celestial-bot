import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    application
}

group = "studios.pinkcloud.celestial"
version = "1.0-SNAPSHOT"

val jvmTarget = 17

repositories {
    mavenCentral()
    // Kord Snapshots Repository (Optional):
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.9.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(jvmTarget)
}

application {
    mainClass.set("MainKt")
}