
plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "studios.pinkcloud.celestial"
version = "1.0-SNAPSHOT"
val jvmTarget = 17
val jdaVersion = "5.0.0-alpha.11"
val Version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://m2.dv8tion.net/releases")
    maven ("https://jitpack.io")

}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("com.charleskorn.kaml:kaml:0.52.0")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("com.akuleshov7:ktoml-source-jvm:0.5.0")
    implementation("com.akuleshov7:ktoml-core:0.5.0")
    implementation("com.akuleshov7:ktoml-file:0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("io.github.freya022:BotCommands:2.10.2")


}


tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("")

    exclude("**/*.kotlin_metadata")
    exclude("**/*.kotlin_builtins")
    exclude("META-INF/")

    archiveFileName.set("Celestial-$Version.jar")
}

kotlin {
    jvmToolchain(jvmTarget)
}

application {
    mainClass.set("studios.pinkcloud.celestial.MainKt")
}