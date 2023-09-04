package studios.pinkcloud.celestial.commands.hypixel.skyblock

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import studios.pinkcloud.celestial.config
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color

class Museum : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "museum",
        description = "Get SkyBlock museum data"
    )
    fun onMuseumCommand(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val apiKey = config!!.tokens.hypixel
        val apiUrl = "https://api.hypixel.net/skyblock/museum"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .header("API-Key", apiKey)
            .build()

        val response: Response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonResponse = JSONObject(response.body?.string())

            if (jsonResponse.getBoolean("success")) {
                val museumData = jsonResponse.getJSONObject("profile")
                val appraisal = museumData.getBoolean("appraisal")

                val embed = EmbedBuilder()
                    .setTitle("SkyBlock Museum Data")
                    .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
                    .addField("Appraisal", appraisal.toString(), false)

                val items = museumData.optJSONObject("items")

                if (items != null && items.length() > 0) {
                    embed.addField("Items", items.toString(), false)
                } else {
                    embed.addField("Items", "No items found.", false)
                }

                val special = museumData.optJSONArray("special")

                if (special != null && special.length() > 0) {
                    embed.addField("Special", special.toString(), false)
                } else {
                    embed.addField("Special", "No special items found.", false)
                }

                event.hook.sendMessageEmbeds(embed.build()).queue()
            } else {
                event.hook.sendMessage("Failed to fetch museum data. Contact Support").queue()
            }
        } else {
            event.hook.sendMessage("Failed to fetch museum data. Contact Support").queue()
        }
    }
}