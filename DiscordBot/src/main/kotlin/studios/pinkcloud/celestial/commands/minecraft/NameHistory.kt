package studios.pinkcloud.celestial.commands.minecraft

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.annotations.AppOption
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import studios.pinkcloud.celestial.enums.Colors
import studios.pinkcloud.celestial.utils.fetchNameHistory
import studios.pinkcloud.celestial.utils.fetchPlayerUUID
import java.awt.Color
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NameHistory : ApplicationCommand() {

    @JDASlashCommand(
        name = "namehistory",
        description = "Get the Minecraft name history of a player"
    )
    fun onSlashCommand(event: SlashCommandInteractionEvent, @AppOption(name = "ign") ign: String) {
        event.deferReply().queue()
        val nameHistoryJson = fetchNameHistory(ign)
        if (nameHistoryJson != null) {
            val embed = buildProfileEmbed(ign, nameHistoryJson)
            event.hook.sendMessageEmbeds(embed).queue()
        } else {
            event.hook.sendMessage("Unable to retrieve name history for $ign").queue()
        }
    }

    private fun buildProfileEmbed(ign: String, profileJson: JsonObject): MessageEmbed {

        val embed = EmbedBuilder()
            .setTitle("Minecraft name data for $ign")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .setDescription("Minecraft profile for player `$ign`")
        val uuid = fetchPlayerUUID(ign)
        val skinURL = "https://skins.mcstats.com/bust/$uuid"
        embed.setThumbnail(skinURL)
        embed.addField("UUID", profileJson["uuid"].asString, false)
        embed.addField("Views", profileJson["views"].asInt.toString(), false)
        val nameHistoryArray = profileJson.getAsJsonArray("name_history")
        val nameHistoryList = mutableListOf<String>()
        for (entry in nameHistoryArray) {
            val name = entry.asJsonObject["name"].asString
            if (name != "-" && nameHistoryList.size < 5) {
                nameHistoryList.add("`$name`")
            }
        }
        if (nameHistoryList.isNotEmpty()) {
            embed.addField("Name History", nameHistoryList.joinToString("\n"), false)
        }
        return embed.build()
    }
}