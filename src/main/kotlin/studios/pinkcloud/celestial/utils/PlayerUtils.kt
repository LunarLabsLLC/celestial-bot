package studios.pinkcloud.celestial.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

fun fetchPlayerUUID(ign: String): String? {
    try {
        val apiUrl = "https://api.mojang.com/users/profiles/minecraft/$ign"
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        val responseCode = connection.responseCode

        if (responseCode == 200) {
            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonElement = JsonParser.parseString(inputStream)
            val jsonObject = jsonElement.asJsonObject
            return jsonObject.get("id").asString
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}
 fun fetchNameHistory(ign: String): JsonObject? {
    val apiUrl = "http://35.75.1.79/profile/$ign"
//this api is so fucking slow its not even a joke but its the only website i can find to get name history
    try {
        val url = URL(apiUrl)
        val scanner = Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")
        val profileData = if (scanner.hasNext()) scanner.next() else ""
        scanner.close()

        return JsonParser.parseString(profileData).asJsonObject
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}