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
import studios.pinkcloud.celestial.api.routing.games.Method
import studios.pinkcloud.celestial.api.routing.games.Stats
import studios.pinkcloud.celestial.api.routing.games.getLocalStats
import studios.pinkcloud.celestial.api.routing.games.getStats
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
    @SerialName("coins")                        val coins: Int
): Stats()

fun Application.bedwarsStats() {
    routing {
        get("/player/bedwars") {
            getStats<GlobalBedwarsStats>("Bedwars") { return@get }
        }
        get("/player/bedwars/eight_one") {
            getLocalStats<GlobalBedwarsStats>("Bedwars","eight_one", Method.PREFIX) {}
        }
    }
}
