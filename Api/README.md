# Celestial Bot API

## Building


```shell
gradle shadowJar
```

output will be under `build/libs/Celestial-Api-{version}.jar`

## Environment

These environment variables must be set before building

| Keys            | Values                          |
|-----------------|---------------------------------|
| `hypixelApiKey` | The Hypixel API Production Key  |
| `redisPassword` | Password for the Redis database |