package studios.pinkcloud.celestial.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.net.URL
fun fetchServerInfo(server: String): JsonObject? {
    val apiUrl = "https://api.mcsrvstat.us/3/$server"
    try {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (connection.responseCode == 200) {
            val response = connection.inputStream.bufferedReader().readText()
            return JsonParser.parseString(response).asJsonObject
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

