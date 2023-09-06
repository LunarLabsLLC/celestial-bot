package studios.pinkcloud.celestial.api

import com.akuleshov7.ktoml.file.TomlFileReader
import com.akuleshov7.ktoml.source.TomlSourceReader
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import okhttp3.OkHttpClient
import org.redisson.Redisson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import studios.pinkcloud.celestial.api.routing.games.bedwars.bedwarsStats
import studios.pinkcloud.celestial.api.routing.games.skywars.skywarsStats

const val hypixelApi = "https://api.hypixel.net"

@Serializable
data class UUID(private val uuid: String) {
    override fun toString(): String = uuid
}

fun getConfig(): ApiConfig? {
    object{}.javaClass.classLoader.getResource("configuration.toml")?.let {
        return TomlSourceReader.decodeFromString<ApiConfig>(it.readText());
    }
    return null
}

private val config = getConfig() ?: throw MissingConfig("Config could not be read, make sure it exists.")
val redisDatabase = redisSetup(config.redis.host, config.redis.port)

fun main() {
    embeddedServer(
        factory = Netty,
        host = config.host,
        port = config.port,
        module = Application::module)
        .start(wait = true)
    redisDatabase.shutdown()
}

val logger: Logger by lazy {
    LoggerFactory
        .getLogger(Application::class.java)!!
}

val hypixelApiKey: String
    get() = System.getenv("hypixelApiKey")

val httpClient: OkHttpClient by lazy { OkHttpClient() }

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    bedwarsStats()
    skywarsStats()
}