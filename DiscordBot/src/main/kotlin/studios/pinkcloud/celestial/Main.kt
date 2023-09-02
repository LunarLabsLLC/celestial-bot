package studios.pinkcloud.celestial

import com.akuleshov7.ktoml.file.TomlFileReader
import com.freya02.botcommands.api.CommandsBuilder
import com.freya02.botcommands.api.Logging
import com.freya02.botcommands.api.builder.TextCommandsBuilder
import com.freya02.botcommands.api.components.DefaultComponentManager
import kotlinx.serialization.decodeFromString
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import studios.pinkcloud.celestial.utils.Configuration
import java.time.Instant
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import studios.pinkcloud.celestial.database.MongoHandler
import mu.KotlinLogging
import mu.KLogger
import studios.pinkcloud.celestial.database.RedisHandler

var jda: JDA? = null
var startTime: Instant? = null
var version = "1.0.0-SNAPSHOT"
val commandsBuilder = CommandsBuilder.newBuilder(1057312650381504543)
val logger: KLogger = KotlinLogging.logger {}
val config = object {}.javaClass.classLoader.getResource("config.toml")
    ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
fun main() {
    jda = JDABuilder.createLight(config!!.tokens.discord).build().awaitReady()
    startTime = Instant.now()
    jda.let {
        it!!.presence.setPresence(Activity.playing(config.botinfo.status), false)
    }

    logger.info(
        "Registering commands...")
    CommandsBuilder.newBuilder(1057312650381504543)
        .textCommandBuilder { textCommandsBuilder: TextCommandsBuilder ->
            textCommandsBuilder.addPrefix(
                "-"
            )
        }
        .build(jda, "studios.pinkcloud.celestial.commands")
    logger.info("Commands registered!")
    logger.info("Connecting to MongoDB...")
    val mongoHandler = MongoHandler()
    logger.info("MongoDB connected!")
    logger.info("Connecting to Redis...")
    val redisHandler = RedisHandler()
    logger.info("Redis connected!")


}

