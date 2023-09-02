package studios.pinkcloud.celestial.commands.impl

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
            .setTitle("Pong!")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .addField("Gateway Ping", "$gatewayPing ms", true)
            .addField("API Latency", "$apiLatency ms", true)
            .addField("Total Latency", "${gatewayPing + apiLatency} ms", true)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)
            .build()
        event.replyEmbeds(embed).queue()
    }
}