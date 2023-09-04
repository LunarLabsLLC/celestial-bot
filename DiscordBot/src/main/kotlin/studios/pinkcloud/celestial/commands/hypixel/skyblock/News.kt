package studios.pinkcloud.celestial.commands.hypixel.skyblock

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import studios.pinkcloud.celestial.config
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color

class News : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "news",
        description = "Get the latest Hypixel SkyBlock news"
    )
    fun onNewsCommand(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()
        val apiUrl = "https://api.hypixel.net/skyblock/news?key=" + config!!.tokens.hypixel
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonResponse = JSONObject(response.body?.string())
            val items = jsonResponse.getJSONArray("items")
            if (items.length() > 0) {
                val latestNews = items.getJSONObject(0)
                val title = latestNews.getString("title")
                val text = latestNews.getString("text")
                val link = latestNews.getString("link")
                val embed = EmbedBuilder()
                    .setTitle(title)
                    .setDescription(text)
                    .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
                    .setDescription("Here is the latest News: " + link)
                    .build()

                event.hook.sendMessageEmbeds(embed).queue()
            } else {
                event.hook.sendMessage("No news found. :(").queue()
            }
        } else {
            event.hook.sendMessage("Failed to fetch news. Contact Support").queue()
        }
    }
}