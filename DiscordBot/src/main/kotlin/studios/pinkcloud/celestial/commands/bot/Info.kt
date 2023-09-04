package studios.pinkcloud.celestial.commands.bot

import com.freya02.botcommands.api.application.ApplicationCommand
import com.freya02.botcommands.api.application.CommandScope
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import studios.pinkcloud.celestial.enums.Colors
import studios.pinkcloud.celestial.startTime
import studios.pinkcloud.celestial.version
import java.awt.Color
import java.time.Duration
import java.time.Instant

class Info : ApplicationCommand() {
    @JDASlashCommand(
        scope = CommandScope.GLOBAL,
        name = "info",
        description = "Get the bot's information"
    )

    fun onSlashCommand(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val uptime = calculateUptime()
        val developers = arrayOf("<@1057312650381504543>", "<@883348946708221952>")
        val programmingLanguages = "<:kotlin:1147577359508197396> Kotlin"
        val framework = "JDA v5.0.0-alpha.11"
        val botVersion = version

        val embed = EmbedBuilder()
            .setTitle("Celestial")
            .setColor(Color.decode(Colors.FIRST_COLOR.hexCode))
            .setDescription("Celestial is a multi-purpose Hypixel-focused Discord bot.")
            .addField("<:uparrow:1147577131631644713> Uptime", uptime, true)
            .addField(":computer: Programming Languages", programmingLanguages, true)
            .addField("<:developer:1147577577574256734> Developers", developers.joinToString(", "), true)
            .addField("<:Discord:1147577856348663909> Framework", framework, true)
            .addField(":robot: Bot Version", botVersion, true)
            .addField("<:Jenkins:1147579719492059247> Jenkins", "[Jenkins](https://ci.bunni.me/job/Celestial/)", true)
            .addField(
                "<:dehGithhubTeam:1147578553865601215> Source Code",
                "[GitHub](https://github.com/PinkCloudStudios/celestial-bot)",
                true
            )
            .setFooter("Requested by ${event.user.asTag}", event.user.effectiveAvatarUrl)
            .build()

        event.hook.sendMessageEmbeds(embed).queue()
    }

    private fun calculateUptime(): String {
        val now = Instant.now()
        val uptimeDuration = Duration.between(startTime, now)
        val days = uptimeDuration.toDays()
        val hours = uptimeDuration.toHours() % 24
        val minutes = uptimeDuration.toMinutes() % 60
        val seconds = uptimeDuration.seconds % 60

        return "${days}d ${hours}h ${minutes}m ${seconds}s"
    }
}