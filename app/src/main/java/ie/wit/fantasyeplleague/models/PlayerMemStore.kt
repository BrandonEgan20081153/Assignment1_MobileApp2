package ie.wit.fantasyeplleague.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PlayerMemStore : PlayerStore {

    val players = ArrayList<eplModels>()

    override fun findAll(): List<eplModels> {
        return players
    }

    override fun create(player: eplModels) {
        player.id= getId()
        players.add(player)
        logAll()
    }

    override fun update(player: eplModels) {
        var foundPlayer: eplModels? = players.find { p -> p.id == player.id }
        if (foundPlayer != null) {
            foundPlayer.name = player.name
            foundPlayer.position = player.position
            foundPlayer.image = player.image
            foundPlayer.lat = player.lat
            foundPlayer.lng = player.lng
            foundPlayer.zoom = player.zoom
            logAll()
        }
    }

    override fun delete(player: eplModels) {
        players.remove(player)
    }

    fun logAll() {
        players.forEach{ i("${it}") }
    }
}