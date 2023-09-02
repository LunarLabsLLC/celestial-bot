package studios.pinkcloud.celestial

import com.akuleshov7.ktoml.file.TomlFileReader
import com.freya02.botcommands.api.CommandsBuilder
import com.freya02.botcommands.api.builder.TextCommandsBuilder
import com.freya02.botcommands.api.components.DefaultComponentManager
import kotlinx.serialization.decodeFromString
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import studios.pinkcloud.celestial.utils.Configuration
import java.time.Instant
import java.util.*


var jda: JDA? = null
var startTime: Instant? = null
var version = "1.0.0-SNAPSHOT"
val commandsBuilder = CommandsBuilder.newBuilder(1057312650381504543)
fun main() {
    val config = object {}.javaClass.classLoader.getResource("config.toml")
        ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
    jda = JDABuilder.createLight(config!!.tokens.discord).build().awaitReady()
    startTime = Instant.now()
    jda.let {
        it!!.presence.setPresence(Activity.playing(config.botinfo.status), false)
    }
    CommandsBuilder.newBuilder(1057312650381504543)
        .textCommandBuilder { textCommandsBuilder: TextCommandsBuilder ->
            textCommandsBuilder.addPrefix(
                "-"
            )
        }
        .build(jda, "studios.pinkcloud.celestial.commands")
}

