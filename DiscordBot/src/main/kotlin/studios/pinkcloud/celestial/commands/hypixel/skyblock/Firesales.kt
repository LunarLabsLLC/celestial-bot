package studios.pinkcloud.celestial.commands.hypixel.skyblock

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import studios.pinkcloud.celestial.config
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

class Firesales : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "firesales",
        description = "Get information about the current Hypixel SkyBlock firesales"
    )
    fun onFiresalesCommand(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()
        val apiUrl = "https://api.hypixel.net/skyblock/firesales"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonResponse = JSONObject(response.body?.string())
            val sales = jsonResponse.getJSONArray("sales")

            if (sales.length() > 0) {
                val embed = EmbedBuilder()
                    .setTitle("Hypixel SkyBlock Firesales")
                    .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))

                for (i in 0 until sales.length()) {
                    val sale = sales.getJSONObject(i)
                    val itemId = sale.getString("item_id")
                    val start = sale.getLong("start")
                    val end = sale.getLong("end")
                    val amount = sale.getInt("amount")
                    val price = sale.getInt("price")

                    val startDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date(start))
                    val endDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date(end))

                    embed.addField(
                        "Item: $itemId",
                        "Start: $startDate\nEnd: $endDate\nAmount: $amount\nPrice: $price",
                        false
                    )
                }

                event.hook.sendMessageEmbeds(embed.build()).queue()
            } else {
                event.hook.sendMessage("No firesales found. :(").queue()
            }
        } else {
            event.hook.sendMessage("Failed to fetch firesales. Contact Support").queue()
        }
    }
}
