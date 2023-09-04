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
import studios.pinkcloud.celestial.config
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

class Auctions : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "auctions",
        description = "Get information about a player's Hypixel SkyBlock auctions"
    )
    fun onAuctionsCommand(event: SlashCommandInteractionEvent, @AppOption(name = "ign") ign: String) {
        event.deferReply().queue()

        val apiUrl = "https://api.hypixel.net/skyblock/auctions?key=${config!!.tokens.hypixel}&player=$ign"
        val client = OkHttpClient()
        val request = Request.Builder().url(apiUrl).build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonResponse = JSONObject(response.body?.string())
            val auctions = jsonResponse.getJSONArray("auctions")

            if (auctions.length() > 0) {
                val maxFieldsPerEmbed = 5 // Maximum number of fields per embed
                val maxEmbeds = 5 // Maximum number of embeds to send
                val totalAuctions = auctions.length()

                // Calculate the number of embeds needed
                val embedCount = minOf((totalAuctions + maxFieldsPerEmbed - 1) / maxFieldsPerEmbed, maxEmbeds)

                for (embedIndex in 0 until embedCount) {
                    val embed =
                        EmbedBuilder().setTitle("$ign's Hypixel SkyBlock Auctions (Page ${embedIndex + 1}/$embedCount)")
                            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))

                    val startIndex = embedIndex * maxFieldsPerEmbed
                    val endIndex = minOf((embedIndex + 1) * maxFieldsPerEmbed, totalAuctions)

                    for (i in startIndex until endIndex) {
                        val auction = auctions.getJSONObject(i)
                        val itemId = auction.getString("item_name")
                        val itemLore = auction.getString("item_lore")
                        val maxLength = 500
                        val itemLoreShortened = if (itemLore.length > maxLength) {
                            val truncatedLore = itemLore.substring(0, maxLength - 3) + "..."
                            "$truncatedLore (Lore too long, truncated)"
                        } else {
                            itemLore
                        }

                        val startingBid = auction.getInt("starting_bid")
                        val highestBidAmount = auction.getInt("highest_bid_amount")

                        val startDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
                            Date(auction.getLong("start"))
                        )
                        val endDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
                            Date(auction.getLong("end"))
                        )

                        embed.addField(
                            "Item: $itemId",
                            "Start: $startDate\nEnd: $endDate\nItem Lore: $itemLoreShortened\nStarting Bid: $startingBid coins\nHighest Bid Amount: $highestBidAmount coins",
                            false
                        )
                    }

                    event.hook.sendMessageEmbeds(embed.build()).queue()
                    if (embedIndex + 1 >= maxEmbeds) {
                        break
                    }
                }
            } else {
                event.hook.sendMessage("No auctions found for $ign. :(").queue()
            }
        } else {
            event.hook.sendMessage("Failed to fetch auctions. Contact Support").queue()
        }
    }

    private fun stripColorCodes(input: String): String {
        return input.replace("§.", "")
    }
}





