import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.function.Consumer

abstract class CommandUtility {
    lateinit var event: SlashCommandInteractionEvent

    abstract fun onSlashCommandInteraction()

    fun sendMessage(message: String, success: Consumer<Message>? = null) {
        val sendMessage = event.hook.sendMessage(message)
        when (success) {
            null -> sendMessage.queue()
            else -> sendMessage.queue(success)
        }
    }

    fun sendEmphemralMessage(message: String, success: Consumer<Message>? = null) {
        val ephemeral = event.hook.sendMessage(message).setEphemeral(true)
        when (success) {
            null -> ephemeral.queue()
            else -> ephemeral.queue(success)
        }
    }
}