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

suspend inline fun <reified T: Stats> RequestBody.getLocalStats(
    gameModeKey: String,
    mode: String,
    method: Method,
    crossinline exitAction: () -> Unit) {
    val properties = T::class.declaredMemberProperties
    val members = mutableListOf<String>()
    val membersFallback = mutableListOf<String>()
    properties.forEach { property ->
        val propertyReflection = property::class
        val fieldName = propertyReflection.findAnnotation<SerialName>()?.value

        if (fieldName == null) {
            members += when (method) {
                Method.SUFFIX -> "${fixMap[property.name]}_$mode"
                Method.PREFIX -> "${mode}_${fixMap[property.name]}"
            }
            membersFallback += property.name
        } else {
            members += when (method) {
                Method.SUFFIX -> "${fieldName}_$mode"
                Method.PREFIX -> "${mode}_$fieldName"
            }
            membersFallback += fieldName
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
            call.respondText("Hypixel Api call failed", status = HttpStatusCode.BadGateway)
            exitAction()
            return
        }

        val stats: JsonElement? = try {
            val element = profile.jsonObject["stats"]?.jsonObject?.get(gameModeKey) ?: JsonNull
            val eObject = element.jsonObject
            val constructedObject = buildJsonObject {
                var iter = 0
                members.forEach {
                    val k = if (eObject[it] == null) membersFallback[iter] else it
                    val currentObject = eObject[k] ?: JsonNull
                    put(k, currentObject)
                    iter++
                }
            }
            constructedObject
        } catch (e: Exception) {
            call.respondText("Error parsing $gameModeKey stats", status = HttpStatusCode.InternalServerError)
            null
        }

        if (stats != null) {
            call.respond<JsonElement>(stats)
        }
    } catch (e: Exception) {
        call.respondText("An error occurred: ${e.message}", status = HttpStatusCode.InternalServerError)
    }
}