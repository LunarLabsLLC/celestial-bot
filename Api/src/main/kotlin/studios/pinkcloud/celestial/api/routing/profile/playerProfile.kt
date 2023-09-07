@file:Suppress("SpellCheckingInspection")

package studios.pinkcloud.celestial.api.routing.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import okhttp3.Request
import studios.pinkcloud.celestial.api.*
import studios.pinkcloud.celestial.api.routing.Global.jsonIgnoreUnknownKeys
import org.redisson.api.RBucket

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
    try {
        logger.debug("Requesting player profile")
        val request = Request.Builder()
            .url("$hypixelApi/player?uuid=$uuid&key=$hypixelApiKey")
            .build()

        httpClient.newCall(request).execute().use { response ->
            val received = response.body?.string()

            if (received != null) {
                val receivedJson = Json.parseToJsonElement(received)
                val success = jsonIgnoreUnknownKeys.decodeFromJsonElement<HypixelSuccess>(receivedJson).success
                val bucket: RBucket<String> = redisDatabase.getBucket(uuid.toString())
                val cachedData = bucket.get()

                if (cachedData != null) {
                    try {
                        return Json.parseToJsonElement(cachedData)
                    } catch (e: Exception) {
                        logger.error("Error parsing cached data: ${e.message}", e)
                    }
                }

                if (success) {
                    val user = receivedJson.jsonObject["player"]
                    val userProfile = UserProfile(
                        uuid.toString(),
                        user?.jsonObject?.get("displayname")?.jsonPrimitive?.content ?: "",
                        user?.jsonObject?.get("newPackageRank")?.jsonPrimitive?.content ?: "",
                        user?.jsonObject?.get("stats") ?: JsonNull
                    )
                    val userProfileJson = Json.encodeToString(userProfile)
                    bucket.set(userProfileJson)
                    return userProfile.stats
                }
            }
        }
    } catch (e: Exception) {
        logger.error("An error occurred: ${e.message}", e)
    }
    return null
}
