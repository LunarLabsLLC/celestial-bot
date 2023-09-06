package studios.pinkcloud.celestial.api.routing

import kotlinx.serialization.json.Json

object Global {
    val jsonIgnoreUnknownKeys = Json { ignoreUnknownKeys = true }
}