package studios.pinkcloud.celestial.api.routing.games

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import okhttp3.Request
import studios.pinkcloud.celestial.api.httpClient
import studios.pinkcloud.celestial.api.hypixelApi
import studios.pinkcloud.celestial.api.hypixelApiKey

@Serializable
data class UUID(val uuid: String) {
    override fun toString(): String = uuid
}

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

private val jsonIgnoreUnknownKeys = Json { ignoreUnknownKeys = true }

fun Application.bedwarsStats() {
    routing {
        get("/player/bedwars") {
            val uuid = call.receive<UUID>()
            val request = Request.Builder()
                .url("$hypixelApi/player?uuid=$uuid&key=$hypixelApiKey")
                .build()

            httpClient.newCall(request).execute().use {response ->
                if(response.isSuccessful) {
                    val received = response.body!!.string()
                    val receivedJson = Json.parseToJsonElement(received)
                    val statsElement = receivedJson
                        .jsonObject["player"]!!
                        .jsonObject["stats"]!!
                        .jsonObject["Bedwars"]!!
                    val stats = jsonIgnoreUnknownKeys.decodeFromJsonElement<GlobalBedwarsStats>(statsElement)
                    call.respond<GlobalBedwarsStats>(stats)
                } else {
                    call.respondText("Hypixel API call failed", status = HttpStatusCode.BadGateway)
                }
            }
        }
    }
}