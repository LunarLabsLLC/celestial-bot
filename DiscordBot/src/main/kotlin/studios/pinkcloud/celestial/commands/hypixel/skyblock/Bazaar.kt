package studios.pinkcloud.celestial.commands.hypixel.skyblock

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.annotations.AppOption
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color

class Bazaar : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "bazaar",
        description = "Get the latest Hypixel SkyBlock bazaar quick buy or quick sell summaries"
    )
    fun onBazaarCommand(event: SlashCommandInteractionEvent, @AppOption(name = "type") type: String) {
        event.deferReply().queue()

        if (type.equals("quickbuy", ignoreCase = true) || type.equals("quicksell", ignoreCase = true)) {
            val apiUrl = "https://api.hypixel.net/skyblock/bazaar"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(apiUrl)
                .build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body?.string())

                if (jsonResponse.has("products")) {
                    val products = jsonResponse.getJSONObject("products")
                    val productIds = products.keys()

                    val embed = EmbedBuilder()
                        .setTitle("Hypixel SkyBlock Bazaar ${type.capitalize()} Summary")
                        .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))

                    var summaryArray: JSONArray? = null

                    // Find the summary based on the product name
                    for (productId in productIds) {
                        val product = products.getJSONObject(productId)
                        val productName = product.getString("product_id")

                        if (productName.equals("${type}_summary", ignoreCase = true)) {
                            summaryArray = product.getJSONArray("${type}_summary")
                            break
                        }
                    }

                    if (summaryArray != null && summaryArray.length() > 0) {
                        embed.setDescription(formatSummary(summaryArray))
                    } else {
                        embed.setDescription("No ${type.capitalize()} data found. :(")
                    }

                    event.hook.sendMessageEmbeds(embed.build()).queue()
                } else {
                    event.hook.sendMessage("${type.capitalize()} data not found. :(").queue()
                }
            } else {
                event.hook.sendMessage("Failed to fetch ${type.capitalize()} data. Contact Support").queue()
            }
        } else {
            event.hook.sendMessage("Invalid option. Use either 'quickbuy' or 'quicksell'.").queue()
        }
    }

    private fun formatSummary(summary: JSONArray): String {
        val builder = StringBuilder()
        for (i in 0 until summary.length()) {
            val entry = summary.getJSONObject(i)
            val price = entry.getDouble("pricePerUnit")
            val amount = entry.getInt("amount")
            builder.append("${i + 1}. Price: $price coins, Amount: $amount\n")
        }
        return builder.toString()
    }
}