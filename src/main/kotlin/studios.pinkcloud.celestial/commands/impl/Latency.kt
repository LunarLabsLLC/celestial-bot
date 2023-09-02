package studios.pinkcloud.celestial.commands.impl

import net.dv8tion.jda.api.EmbedBuilder
import studios.pinkcloud.celestial.commands.CommandAbstract
import studios.pinkcloud.celestial.commands.CommandInfo
import studios.pinkcloud.celestial.commands.RegisterCommand
import java.awt.Color

@RegisterCommand
@CommandInfo(name = "latency", description = "responds with bots latency")
class Latency: CommandAbstract(){
    override fun onSlashCommandInteraction() {
        val gatewayPing = event.jda.gatewayPing
        val apiLatency =event.jda.restPing.complete()
        val embed = EmbedBuilder()
            .setTitle("Pong!")
            .setColor(Color.decode("#00FF00"))
            .addField("Gateway Ping", "$gatewayPing ms", true)
            .addField("API Latency", "$apiLatency ms", true)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)
            .build()
        event.replyEmbeds(embed).queue()
    }
}