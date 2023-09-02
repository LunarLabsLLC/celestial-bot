package studios.pinkcloud.celestial

import com.akuleshov7.ktoml.file.TomlFileReader
import kotlinx.serialization.decodeFromString
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import studios.pinkcloud.celestial.commands.CommandManager
import studios.pinkcloud.celestial.utils.Configuration

var jda: JDA? = null
fun main() {
    val config = object {}.javaClass.classLoader.getResource("config.toml")
        ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
    jda = JDABuilder.createLight(config!!.tokens.discord).build().awaitReady()
    jda.let {
        it!!.presence.setPresence(Activity.playing(config.botinfo.status), false)
    }


    val commandManager = CommandManager.getInstance()
    commandManager.init()


}


