package studios.pinkcloud.celestial.utils

import net.dv8tion.jda.api.requests.restaction.CommandCreateAction

    fun CommandCreateAction.queueIgnoreException() {
        try {
            queue()
        } catch (e: Exception) {
            // Ignore
        }
    }
