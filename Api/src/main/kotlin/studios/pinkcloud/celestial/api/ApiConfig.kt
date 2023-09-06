package studios.pinkcloud.celestial.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiConfig(val host: String, val port: Int, val redis: Redis)

@Serializable
data class Redis(val host: String, val port: Int)
