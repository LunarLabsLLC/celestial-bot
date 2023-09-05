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
import studios.pinkcloud.celestial.api.UUID
import studios.pinkcloud.celestial.api.httpClient
import studios.pinkcloud.celestial.api.hypixelApi
import studios.pinkcloud.celestial.api.hypixelApiKey


@Serializable
data class GlobalSkywarsStats (
    @SerialName("wins")                   val wins: Int,
    @SerialName("losses")                 val losses: Int,
    @SerialName("kills")                  val kills: Int,
    @SerialName("deaths")                 val deaths: Int,
    @SerialName("coins")                  val coins: Int,
    @SerialName("heads")                  val heads: Int,
    @SerialName("souls")                  val souls: Int,
    @SerialName("opals")                  val opals: Int,
    @SerialName("angel_of_death_level")   val angelOfDeathLevel: Int,
    @SerialName("games")                  val games: Int,
    @SerialName("time_played")            val timePlayed: Int,
    )

data class LocalSkyWarsStats(
    val prefix: String,
)

private val jsonIgnoreUnknownKeys = Json { ignoreUnknownKeys = true }

fun Application.skywarsStats() {
    routing {
        get("/player/skywars") {
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
                        .jsonObject["SkyWars"]!!
                    val stats = jsonIgnoreUnknownKeys.decodeFromJsonElement<GlobalSkywarsStats>(statsElement)
                    call.respond<GlobalSkywarsStats>(stats)
                } else {
                    call.respondText("Hypixel API call failed", status = HttpStatusCode.BadGateway)
                }
            }
        }
    }
}