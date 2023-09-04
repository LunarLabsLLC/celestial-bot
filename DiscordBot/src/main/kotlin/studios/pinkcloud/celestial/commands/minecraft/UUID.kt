package studios.pinkcloud.celestial.commands.minecraft

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.annotations.AppOption
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import studios.pinkcloud.celestial.enums.Colors
import studios.pinkcloud.celestial.utils.fetchPlayerUUID
import java.awt.Color


class UUID : ApplicationCommand() {

    @JDASlashCommand(
        scope = CommandScope.GUILD, name = "uuid", description = "Get the Minecraft UUID of a player"
    )

    fun onSlashCommand(
        event: SlashCommandInteractionEvent,
        @AppOption(name = "ign", description = "Player's Minecraft IGN") ign: String
    ) {
        val ign = event.getOption("ign")?.asString ?: return
        val uuid = fetchPlayerUUID(ign)
        val embed = EmbedBuilder().setTitle("Player UUID").setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .setDescription("UUID of player `$ign`").addField("UUID", uuid.toString(), false)
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl).build()
        event.hook.sendMessageEmbeds(embed).queue()
    }
}