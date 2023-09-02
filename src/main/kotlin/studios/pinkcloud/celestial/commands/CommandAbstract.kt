@file:JvmName("CommandAbstract")
package studios.pinkcloud.celestial.commands

import CommandUtility

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import studios.pinkcloud.celestial.enums.SlashCommandType
import studios.pinkcloud.celestial.jda
import studios.pinkcloud.celestial.utils.queueIgnoreException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class CommandAbstract : CommandUtility() {
    protected open val optionDataArray = arrayOf<OptionData>()

    var commandInfo: CommandInfo

    var commandName: String
    var commandDescription: String

    init {
        commandInfo = javaClass.getAnnotation(CommandInfo::class.java) ?: throw IllegalArgumentException("Command class must be annotated with @CommandInfo")
        commandName = commandInfo.name
        commandDescription = commandInfo.description
    }

    fun execute(event: SlashCommandInteractionEvent) {
        this.event = event
        if (commandInfo.autoDeferReply) {
            event.deferReply(true).queue()
        }
        onSlashCommandInteraction()
    }

    fun load() {
        if (commandInfo.autoRegisterSlashCommand) {
            val executorService = Executors.newFixedThreadPool(20)
            when (commandInfo.slashCommandType) {
                SlashCommandType.ALL -> {
                    executorService.submit {
                        val upsertCommand = jda!!.upsertCommand(commandName, commandDescription)
                        if (optionDataArray.isNotEmpty()) {
                            upsertCommand.addOptions(*optionDataArray)
                        }
                        upsertCommand.queueIgnoreException()
                    }
                }
                SlashCommandType.GUILD_ALL -> {
                    jda!!.guilds.forEach {
                        executorService.submit {
                            val upsertCommand = it.upsertCommand(commandName, commandDescription)
                            if (optionDataArray.isNotEmpty()) {
                                upsertCommand.addOptions(*optionDataArray)
                            }
                            upsertCommand.queueIgnoreException()
//                        println("Registered command $commandName in guild ${it.name}")
                        }
                    }
                }
                SlashCommandType.GUILD_SPECIFIC -> {
                    commandInfo.guilds.forEach {
                        executorService.submit {
                            val upsertCommand = jda!!.getGuildById(it)?.upsertCommand(commandName, commandDescription)
                            if (optionDataArray.isNotEmpty()) {
                                upsertCommand?.addOptions(*optionDataArray)
                            }
                            upsertCommand?.queueIgnoreException()
                        }
                    }
                }
            }
            Thread {
                executorService.shutdown()
                val awaitTermination = executorService.awaitTermination(3, TimeUnit.MINUTES)
                if (!awaitTermination) {
                    executorService.shutdownNow()
                }
            }.start()
        }
    }

    fun isThisClass(event: SlashCommandInteractionEvent): Boolean {
        if (event.name != commandName) {
            return false
        }
        /*val options = event.options
        if (options.size != optionDataArray.size) {
            return false
        }
        for (i in 0 until options.size) {
            try {
                val option = options[i]
                val optionData = optionDataArray[i]

                if (option.type != optionData.type) {
                    return false
                }
                if (option.name != optionData.name) {
                    return false
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                return false
            }
        }*/
        return true
    }
}