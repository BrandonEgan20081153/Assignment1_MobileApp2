package ie.wit.fantasyeplleague.models

interface PlayerStore {
    fun findAll(): List<eplModels>
    fun create(player: eplModels)
    fun update(player: eplModels)
    fun delete(player: eplModels)
}