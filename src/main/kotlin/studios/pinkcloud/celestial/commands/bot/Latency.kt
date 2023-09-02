package studios.pinkcloud.celestial.commands.bot

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import studios.pinkcloud.celestial.enums.Colors
import java.awt.Color

class Latency : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "ping",
        description = "Pong!"
    )
    fun onSlashCommand(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val gatewayPing = event.jda.gatewayPing
        val apiLatency = event.jda.restPing.complete()

        val embed = EmbedBuilder()
            .setTitle("Pong! We got your latency")
            .setDescription("Celestial seems to be alive!")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .addField("<a:beat:1147561740058501171> Gateway Ping", "$gatewayPing ms", true)
            .addField("<:wifi:1147565931493589114> API Latency", "$apiLatency ms", true)
            .addField(":robot: Total Latency", "${gatewayPing + apiLatency} ms", true)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)
            .build()

        event.hook.sendMessageEmbeds(embed).queue()
    }
}