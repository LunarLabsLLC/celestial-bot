package studios.pinkcloud.celestial.database

import com.akuleshov7.ktoml.file.TomlFileReader
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import kotlinx.serialization.decodeFromString
import org.bson.Document
import studios.pinkcloud.celestial.utils.Configuration

class MongoHandler {
    val config = object {}.javaClass.classLoader.getResource("config.toml")
        ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
    private val mongoClient = MongoClients.create(config!!.mongo.url)
    private val database = mongoClient.getDatabase(config!!.mongo.database)

    fun getCollection(collectionname: String): MongoCollection<Document> {
        return database.getCollection(collectionname)
    }

//    add methods later if needed
}