package studios.pinkcloud.celestial.api

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import studios.pinkcloud.celestial.api.routing.games.bedwarsStats

const val hypixelApi = "https://api.hypixel.net"

fun main() {
    embeddedServer(
        factory = Netty,
        host = "0.0.0.0",
        port = 8080,
        module = Application::module)
        .start(wait = true)
}

val hypixelApiKey: String
    get() = System.getenv("hypixelApiKey")

val httpClient: OkHttpClient by lazy { OkHttpClient() }

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    bedwarsStats()
}