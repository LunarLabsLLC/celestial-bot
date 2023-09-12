package studios.pinkcloud.celestial.api.routing.games.skywars

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import studios.pinkcloud.celestial.api.module
import kotlin.test.Test

class SkywarsKtTest {

    @Test
    fun testGetPlayerSkywarsId() = testApplication {
        application {
            module()
        }
        client.get("/player/skywars/{id}").apply {
            TODO("Please write your test here")
        }
    }
}