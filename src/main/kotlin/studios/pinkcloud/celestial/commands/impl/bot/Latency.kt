package studios.pinkcloud.celestial.commands.impl.bot

import net.dv8tion.jda.api.EmbedBuilder
import studios.pinkcloud.celestial.commands.CommandAbstract
import studios.pinkcloud.celestial.commands.CommandInfo
import studios.pinkcloud.celestial.commands.RegisterCommand
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color

@RegisterCommand
@CommandInfo(name = "latency", description = "responds with bots latency", autoDeferReply = false)
class Latency: CommandAbstract(){
    override fun onSlashCommandInteraction() {
        val gatewayPing = event.jda.gatewayPing
        val apiLatency =event.jda.restPing.complete()
        val embed = EmbedBuilder()
            .setTitle("Pong! We got your latency")
            .setDescription("Celestial seems to be alive!")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .addField("<a:beat:1147561740058501171> Gateway Ping", "$gatewayPing ms", true)
            .addField("<:wifi:1147565931493589114> API Latency", "$apiLatency ms", true)
            .addField(":robot: Total Latency", "${gatewayPing + apiLatency} ms", true)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)
            .build()
        event.replyEmbeds(embed).queue()
    }
}