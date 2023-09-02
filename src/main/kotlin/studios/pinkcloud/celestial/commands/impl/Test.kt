package studios.pinkcloud.celestial.commands.impl

import studios.pinkcloud.celestial.commands.CommandAbstract
import studios.pinkcloud.celestial.commands.CommandInfo
import studios.pinkcloud.celestial.commands.RegisterCommand

@RegisterCommand
@CommandInfo(name = "test", description = "test command")
class Test: CommandAbstract(){
    override fun onSlashCommandInteraction() {
            sendMessage("test")

    }
}