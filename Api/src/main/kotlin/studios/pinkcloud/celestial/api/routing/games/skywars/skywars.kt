package studios.pinkcloud.celestial.api.routing.games.skywars

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import studios.pinkcloud.celestial.api.routing.games.Method
import studios.pinkcloud.celestial.api.routing.games.Stats
import studios.pinkcloud.celestial.api.routing.games.getLocalStats
import studios.pinkcloud.celestial.api.routing.games.getStats

@Serializable
data class GlobalSkywarsStats (
    @SerialName("wins")                   val wins: Int,
    @SerialName("losses")                 val losses: Int,
    @SerialName("kills")                  val kills: Int,
    @SerialName("deaths")                 val deaths: Int,
    @SerialName("coins")                  val coins: Int,
    @SerialName("heads")                  val heads: Int,
    @SerialName("souls")                  val souls: Int,
    @SerialName("time_played")            val timePlayed: Int,
    @SerialName("opals")                  val opals: Int,
    @SerialName("angel_of_death_level")   val angelOfDeathLevel: Int,
    @SerialName("games")                  val games: Int,
    @SerialName("lab_win")                val laboratoryWins: Int
): Stats()

private val gameModes = arrayOf(
    "team_normal",
    "team_insane",
    "team",
    "solo",
    "solo_normal",
    "solo_insane",
    "mega",
    "mega_normal",
    "mega_insane",
    "lucky_blocks_lab",
    "lucky_blocks_lab_solo",
    "lucky_blocks_lab_team",
    "rush_lab",
    "rush_lab_solo",
    "rush_lab_team",
    "slime_lab",
    "slime_lab_solo",
    "slime_lab_team",
    "hunters_vs_beasts_lab",
    "hunters_vs_beasts_lab_solo",
    "hunters_vs_beasts_lab_team"
)

fun Application.skywarsStats() {
    routing {
        get("/player/skywars") {
            getStats<GlobalSkywarsStats>("SkyWars") { return@get }
        }
        get("/player/skywars/{id}") {
            val param: String? = call.parameters["id"]
            if (param == null) call.respondText("Invalid game mode id", status = HttpStatusCode.BadRequest)
            if (param !in gameModes) call.respondText("Invalid game mode id", status = HttpStatusCode.BadRequest)
            getLocalStats<GlobalSkywarsStats>("SkyWars", param!!, Method.SUFFIX) {};
        }
    }
}