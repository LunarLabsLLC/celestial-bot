package studios.pinkcloud.celestial.utils

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val tokens: Tokens, val botinfo: BotInfo, val mongo: Mongo, val redis: Redis)

@Serializable
data class Tokens(val discord: String, val hypixel: String)

@Serializable
data class BotInfo(val status: String)

@Serializable
data class Mongo(val url: String, val database: String, val collection: String)

@Serializable
data class Redis(val host: String, val port: Int, val password: String)
