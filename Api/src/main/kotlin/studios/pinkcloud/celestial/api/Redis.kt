package studios.pinkcloud.celestial.api

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

fun redisSetup(host: String, port: Int): RedissonClient {
    val config = Config()
    config
        .useSingleServer()
        .setAddress("redis://$host:$port")
        .setPassword(System.getenv("redisPassword"))
    return Redisson.create(config)
}