package studios.pinkcloud.celestial.api.routing.games

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import studios.pinkcloud.celestial.api.UUID
import studios.pinkcloud.celestial.api.routing.Global
import studios.pinkcloud.celestial.api.routing.profile.playerProfile

suspend inline fun <reified T: Stats> PipelineContext<Unit, ApplicationCall>.getStats(gameModeKey: String, exitAction: () -> Unit) {
    try {
        val uuid = kotlin.runCatching { call.receiveNullable<UUID>() }.getOrNull()

        if (uuid == null) {
            call.respondText("Invalid UUID provided", status = HttpStatusCode.BadRequest)
            exitAction()
            return
        }

        val profile = playerProfile(uuid)

        if (profile == null) {
            call.respondText("Hypixel Api call failed", status = HttpStatusCode.BadGateway)
            exitAction()
            return
        }

        val stats: T? = try {
            Global.jsonIgnoreUnknownKeys
                .decodeFromJsonElement<T>(
                    profile.jsonObject["stats"]?.jsonObject?.get(gameModeKey) ?: JsonNull
                )
        } catch (e: Exception) {
            call.respondText("Error parsing $gameModeKey stats", status = HttpStatusCode.InternalServerError)
            null
        }

        if (stats != null) {
            call.respond(stats)
        }
    } catch (e: Exception) {
        call.respondText("An error occurred: ${e.message}", status = HttpStatusCode.InternalServerError)
    }
}