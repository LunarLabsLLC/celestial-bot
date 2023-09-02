package studios.pinkcloud.celestial.utils

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val tokens: Tokens, val botinfo: BotInfo)

@Serializable
data class Tokens(val discord: String, val hypixel: String)

@Serializable
data class BotInfo(val status: String)

