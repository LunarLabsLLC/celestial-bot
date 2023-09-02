package studios.pinkcloud.celestial.utils

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val tokens: Tokens)

@Serializable
data class Tokens(val discord: String, val hypixel: String)

