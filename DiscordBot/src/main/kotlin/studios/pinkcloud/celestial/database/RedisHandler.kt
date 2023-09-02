package studios.pinkcloud.celestial.database

import com.akuleshov7.ktoml.file.TomlFileReader
import kotlinx.serialization.decodeFromString
import redis.clients.jedis.Jedis
import studios.pinkcloud.celestial.utils.Configuration

class RedisHandler {
    val config = object {}.javaClass.classLoader.getResource("config.toml")
        ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
    private val jedis = Jedis(config!!.redis.host, config!!.redis.port, false
    ).also {
        it.auth(config!!.redis.password)
    }
}