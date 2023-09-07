package studios.pinkcloud.celestial.api.routing.games.skywars

import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
    @SerialName("games")                  val games: Int
): Stats()

private val jsonIgnoreUnknownKeys = Json { ignoreUnknownKeys = true }

fun Application.skywarsStats() {
    routing {
        get("/player/skywars") {
            getStats<GlobalSkywarsStats>("SkyWars") { return@get }
        }
        get("/player/skywars/solo") {
            getLocalStats<GlobalSkywarsStats>("SkyWars", "solo", Method.SUFFIX) {}
        }
    }
}