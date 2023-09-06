package studios.pinkcloud.celestial.api.routing.games.bedwars

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import studios.pinkcloud.celestial.api.*
import studios.pinkcloud.celestial.api.routing.Global.jsonIgnoreUnknownKeys
import studios.pinkcloud.celestial.api.routing.profile.playerProfile


@Serializable
data class GlobalBedwarsStats (
    @SerialName("Experience")                   val experience: Int,
    @SerialName("games_played_bedwars")         val gamesPlayed: Int,
    @SerialName("kills_bedwars")                val kills: Int,
    @SerialName("deaths_bedwars")               val deaths: Int,
    @SerialName("losses_bedwars")               val loses: Int,
    @SerialName("beds_broken_bedwars")          val bedsBroken: Int,
    @SerialName("_items_purchased_bedwars")     val itemsPurchased: Int,
    @SerialName("beds_lost_bedwars")            val bedsLost: Int,
    @SerialName("void_kills_bedwars")           val voidKills: Int,
    @SerialName("resources_collected_bedwars")  val collectedResources: Int,
    @SerialName("final_kills_bedwars")          val finalKills: Int,
    val coins: Int,
)

data class LocalBedwarsStats(
    val prefix: String,
)

fun Application.bedwarsStats() {
    routing {
        get("/player/bedwars") {
            val uuid = call.receive<UUID>()
            val profile = playerProfile(uuid)
            if (profile == null) suspend {
                call.respondText("Hypixel Api call failed", status = HttpStatusCode.BadGateway)
            }

            var stats: GlobalBedwarsStats? = null

            try {
                stats = jsonIgnoreUnknownKeys
                    .decodeFromJsonElement<GlobalBedwarsStats>(
                        profile!!.jsonObject["stats"]!!.jsonObject["Bedwars"]!!
                    )
            } catch (e: NullPointerException) {
                call.respondText("No UUID provided or Malformed UUID", status = HttpStatusCode.BadRequest)
            }

            call.respond<GlobalBedwarsStats>(stats!!)
        }
    }
}