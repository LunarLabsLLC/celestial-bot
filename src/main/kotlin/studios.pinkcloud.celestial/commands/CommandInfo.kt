package studios.pinkcloud.celestial.commands

import studios.pinkcloud.celestial.enums.SlashCommandType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandInfo(
    val name: String,
    val description: String,
    val slashCommandType: SlashCommandType = SlashCommandType.GUILD_ALL,
    val guilds: LongArray = [],
    val autoDeferReply: Boolean = true,
    val autoRegisterSlashCommand: Boolean = true,
    val autoRegisterClass: Boolean = true,
)