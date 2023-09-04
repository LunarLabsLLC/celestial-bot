package studios.pinkcloud.celestial.commands.minecraft

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.annotations.AppOption
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import studios.pinkcloud.celestial.enums.Colors
import studios.pinkcloud.celestial.utils.fetchPlayerUUID

import java.awt.Color

class Skin : ApplicationCommand() {
    @JDASlashCommand(
        name = "skin", description = "Get the Minecraft skin of a player"
    )

    fun onSlashCommand(event: SlashCommandInteractionEvent, @AppOption(name = "ign") ign: String) {

        val uuid = fetchPlayerUUID(ign)

        if (uuid != null) {
            val skinURL = "https://skins.mcstats.com/body/front/$uuid"

            val embed = EmbedBuilder().setTitle("Minecraft Skin").setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
                .setDescription("Skin of player `$ign`").addField("UUID", uuid.toString(), false)
                .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)

                .setImage(skinURL).build()

            event.hook.sendMessageEmbeds(embed).queue()
        } else {
            event.hook.sendMessage("Unable to retrieve player UUID for $ign").queue()
        }
    }

}