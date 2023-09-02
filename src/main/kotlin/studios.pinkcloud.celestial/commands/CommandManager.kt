package studios.pinkcloud.celestial.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import studios.pinkcloud.celestial.jda
import studios.pinkcloud.celestial.utils.ClassScanner



private val commands = mutableListOf<CommandAbstract>()

class CommandManager private constructor() : ListenerAdapter() {
    companion object {
        @JvmStatic
        private val INSTANCE = CommandManager()

        @JvmStatic
        fun getInstance() = INSTANCE
    }

    fun init() {
        val classList = scanClasses()
        classList.forEach(::registerCommand)
        commands.forEach(CommandAbstract::load)
        jda!!.addEventListener(this)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        commands.filter { it.isThisClass(event) }.forEach { it.execute(event) }
    }

    fun getAllRegisteredCommands() = commands.toList()

    private fun scanClasses(): List<Class<*>> {
        val list = mutableListOf<Class<*>>()
        ClassScanner.getInstance().getAllClasses().forEach {
            var valid = false
            val annotations = it.annotations
            if (annotations.isNotEmpty()) {
                annotations.forEach { it2 ->
                    if (checkValidClass(it, it2)) {
                        var superClass = it
                        while (superClass != CommandAbstract::class.java) {
                            if (superClass.superclass == null || superClass.superclass == java.lang.Object::class.java) {
                                break
                            }
                            superClass = superClass.superclass
                        }
                        if (superClass == CommandAbstract::class.java) {
                            valid = true
                        }
                    }
                }
            }
            if (valid) {
                list.add(it)
            }
        }
        return list
    }

    private fun checkValidClass(clazz: Class<*>, annotation: Annotation): Boolean {
        if (annotation.annotationClass == CommandInfo::class) {
            val commandInfo = annotation as CommandInfo
            if (commandInfo.autoRegisterClass) {
                return true
            }
        }

        if (annotation.annotationClass == RegisterCommand::class) {
            if (clazz.annotations.any { it.annotationClass == CommandInfo::class }) {
                return true
            }
        }

        return false
    }


    private fun registerCommand(command: CommandAbstract) {
        if (commands.none { it.commandName == command.commandName }) {
            commands.add(command)
        } else {
            println("Command ${command.commandName} is already registered!")
        }    }

    private fun registerCommand(clazz: Class<*>) {
        registerCommand(clazz.newInstance() as CommandAbstract)
    }
}