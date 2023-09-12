package studios.pinkcloud.celestial.api.routing.games

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.*
import studios.pinkcloud.celestial.api.UUID
import studios.pinkcloud.celestial.api.routing.profile.playerProfile
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

typealias RequestBody = PipelineContext<Unit, ApplicationCall>

enum class Method {
    PREFIX,
    SUFFIX
}

val fixMap = mapOf(
    "angelOfDeathLevel" to "angel_of_death_level",
    "timePlayed" to "time_played"
)

suspend inline fun <reified T : Stats> RequestBody.getLocalStats(
    gameModeKey: String,
    mode: String,
    method: Method,
    crossinline exitAction: () -> Unit
) {
    val properties = T::class.declaredMemberProperties
    val statsMapping = mutableMapOf<String, String>()

    properties.forEach { property ->
        val fieldName = property.findAnnotation<SerialName>()?.value ?: property.name
        statsMapping[fieldName] = when (method) {
            Method.SUFFIX -> "${fieldName}_$mode"
            Method.PREFIX -> "${mode}_$fieldName"
        }
    }

    try {
        val uuid = runCatching { call.receiveNullable<UUID>() }.getOrNull()

        if (uuid == null) {
            call.respondText("Invalid UUID provided", status = HttpStatusCode.BadRequest)
            exitAction()
            return
        }

        val profile = playerProfile(uuid)

        if (profile == null) {
            call.respondText("Hypixel API call failed", status = HttpStatusCode.BadGateway)
            exitAction()
            return
        }

        val stats: JsonObject = try {
            val element = profile.jsonObject["stats"]?.jsonObject?.get(gameModeKey) ?: JsonObject(emptyMap())
            val mappedStats = buildJsonObject {
                statsMapping.forEach { (original, mapped) ->
                    val originalValue = element.jsonObject[mapped]?.jsonPrimitive
                    if (originalValue != null) {
                        put(mapped, originalValue)
                    }
                }
            }
            mappedStats
        } catch (e: Exception) {
            call.respondText("Error parsing $gameModeKey stats", status = HttpStatusCode.InternalServerError)
            return
        }

        call.respond<JsonObject>(stats)
    } catch (e: Exception) {
        call.respondText("An error occurred: ${e.message}", status = HttpStatusCode.InternalServerError)
    }
}
