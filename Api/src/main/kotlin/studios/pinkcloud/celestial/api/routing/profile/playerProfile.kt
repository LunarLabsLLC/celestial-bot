@file:Suppress("SpellCheckingInspection")

package studios.pinkcloud.celestial.api.routing.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import okhttp3.Request
import org.redisson.Redisson
import studios.pinkcloud.celestial.api.*
import studios.pinkcloud.celestial.api.routing.Global.jsonIgnoreUnknownKeys

@Serializable
data class HypixelSuccess(@SerialName("success") val success: Boolean)

@Serializable
data class UserProfile(
    val uuid: String,
    @SerialName("displayname") val name: String,
    @SerialName("newPackageRank") val rank: String,
    val stats: JsonElement
)

fun playerProfile(uuid: UUID): JsonElement? {
    logger.debug("Requesting player profile")
    val request = Request.Builder()
        .url("$hypixelApi/player?uuid=$uuid&key=$hypixelApiKey")
        .build()
    httpClient.newCall(request).execute().use { response ->
        val received = response.body!!.string()
        val receivedJson = Json.parseToJsonElement(received)
        val success = jsonIgnoreUnknownKeys.decodeFromJsonElement<HypixelSuccess>(receivedJson).success
        val bucket = redisDatabase.getBucket<String>(uuid.toString())
        return if(success) {
            val user = receivedJson.jsonObject["player"]
            bucket.set(Json.encodeToString(UserProfile(
                uuid.toString(),
                user!!.jsonObject["displayname"]?.jsonPrimitive.toString(),
                user.jsonObject["newPackageRank"]?.jsonPrimitive.toString(),
                user.jsonObject["stats"]!!
            ))
            )
            user
        } else {
            bucket.get()?.let {
                Json.encodeToJsonElement(it)
            }
            null
        }
    }
}