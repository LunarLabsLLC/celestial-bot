package studios.pinkcloud.celestial.commands.minecraft

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.annotations.AppOption
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import studios.pinkcloud.celestial.enums.Colors
import studios.pinkcloud.celestial.utils.fetchPlayerUUID
import java.awt.Color

class CapeCommand : ApplicationCommand() {
    @JDASlashCommand(
        name = "cape",
        description = "Get the Minecraft cape of a player"
    )
    fun onSlashCommand (event: SlashCommandInteractionEvent, @AppOption(name = "ign") ign: String) {
        event.deferReply().queue()
        val capeURL = "http://s.optifine.net/capes/$ign.png"

        val embed = EmbedBuilder()
            .setTitle("Minecraft Cape")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .setDescription("Cape of player `$ign`")
            .addField("IGN", ign.toString(), false)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)

            .setImage(capeURL)
            .build()

        event.hook.sendMessageEmbeds(embed).queue()
    }
}
