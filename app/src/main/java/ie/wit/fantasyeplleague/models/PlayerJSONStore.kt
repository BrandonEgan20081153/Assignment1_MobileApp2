package ie.wit.fantasyeplleague.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.fantasyeplleague.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "player.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<eplModels>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PlayerJSONStore(private val context: Context) : PlayerStore {

    var players = mutableListOf<eplModels>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<eplModels> {
        logAll()
        return players
    }

    override fun create(player: eplModels) {
        player.id = generateRandomId()
        players.add(player)
        serialize()
    }


    override fun update(player: eplModels) {
        // todo
        val playerList = findAll() as ArrayList<eplModels>
        var foundPlayer: eplModels? =playerList.find { p -> p.id == player.id }
        if (foundPlayer != null) {
            foundPlayer.name = player.name
            foundPlayer.position = player.position
            foundPlayer.image = player.image
            foundPlayer.lat = player.lat
            foundPlayer.lng = player.lng
            foundPlayer.zoom = player.zoom
        }
        serialize()
    }

    override fun delete(player: eplModels) {
        players.remove(player)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(players, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        players = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        players.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }



}