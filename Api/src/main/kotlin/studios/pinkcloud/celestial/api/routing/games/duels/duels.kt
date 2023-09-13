package studios.pinkcloud.celestial.api.routing.games.duels

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import studios.pinkcloud.celestial.api.UUID
import studios.pinkcloud.celestial.api.routing.profile.playerProfile

sealed interface Parameters {
    val serialName: Array<out String>
}

enum class PlayerCounts(override vararg val serialName: String): Parameters {
    DUEL("duel"),
    DOUBLE("doubles"),
    FOUR("four")
}

enum class WinStreaks(override vararg val  serialName: String): Parameters {
    CURRENT("current"),
    BEST("best")
}

enum class GameModes(override vararg val serialName: String): Parameters {
    BRIDGE("bridge"),
    UHC("uhc"),
    CLASSIC("classic"),
    BLITZ("blitz"),
    BOW("bow"),
    BOWSPLEEF("bowspleef"),
    BOXING("boxing"),
    COMBO("combo"),
    MEGAWALLS("mega_walls"),
    NODEBUFF("no_debuff"),
    OP("op"),
    PARKOUR("parkour"),
    SUMO("sumo"),
    SKYWARS("sw")
}

val duelStats = arrayOf(
    "losses",
    "deaths",
    "kills",
    "wins"
)

fun isValid(key: String?, entries: Array<out String>): Boolean {
    return if (key == null) {
        false
    } else {
        entries.find { it == key } != null
    }
}

sealed interface DuelDataInterface

@Serializable
data class DuelData (
    val gameMode: String,
    val playerCount: String,
    val uuid: String
): DuelDataInterface

@Serializable
data class DuelStreakData(
    val gameMode: String?,
    val streaks: String,
    val uuid: String
): DuelDataInterface

suspend inline fun <reified T: Parameters> enumNames(emptyInstance: T): Array<out String> {
    return mutableListOf<String>()
        .also{ outer ->
            when(emptyInstance) {
                is PlayerCounts -> {
                    PlayerCounts.entries.forEach {
                        outer += it.serialName
                    }
                }
                is WinStreaks -> {
                    WinStreaks.entries.forEach {
                        outer += it.serialName
                    }
                }
                is GameModes -> {
                    GameModes.entries.forEach {
                        outer += it.serialName
                    }
                }
            }
            GameModes.entries.forEach {
                outer += it.serialName
            }
        }
        .toTypedArray()
}

suspend inline fun <reified T: DuelDataInterface> PipelineContext<Unit, ApplicationCall>.validateData(): T {
    val data = call.receive<T>()
    val validity = when(data) {
        is DuelData -> isValid(data.gameMode, enumNames(GameModes.BOW))
                && isValid(data.playerCount, enumNames(PlayerCounts.DOUBLE))
        is DuelStreakData -> isValid(data.gameMode, enumNames(GameModes.BOW))
                && isValid(data.streaks, enumNames(WinStreaks.BEST))
        else -> false
    }

    if (!validity) call.respondText("Invalid Information", status = HttpStatusCode.BadRequest)
    return data
}

fun Application.duelsStats() {
    routing {
        get("/player/duels") {
            val data = validateData<DuelData>()
            val profile = playerProfile(UUID(data.uuid))
            if (profile == null) call.respondText("Invalid uuid", status = HttpStatusCode.BadRequest)

            val stats = buildJsonObject {
                duelStats.forEach { stat ->
                    val completeStat = "${data.gameMode}_${data.playerCount}_${stat}"
                    val innerData = profile
                        ?.jsonObject?.get("stats")
                        ?.jsonObject?.get("Duels")
                        ?.jsonObject?.get(completeStat)
                        ?.jsonPrimitive ?: JsonNull

                    put(completeStat, innerData)
                }
            }

            call.respond<JsonObject>(stats)
        }

        get("/player/duels/streak") {
            val data = validateData<DuelStreakData>()

            val profile = playerProfile(UUID(data.uuid))
            if (profile == null) call.respondText("Invalid uuid", status = HttpStatusCode.BadRequest)


        }
    }
}