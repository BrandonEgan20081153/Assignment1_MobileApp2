package ie.wit.fantasyeplleague.main

import android.app.Application
//import ie.wit.fantasyeplleague.models.eplModels
import ie.wit.fantasyeplleague.models.PlayerJSONStore
import ie.wit.fantasyeplleague.models.PlayerStore
import ie.wit.fantasyeplleague.models.PlayerMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var players: PlayerStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        players = PlayerJSONStore(applicationContext)
        i("EPL App started")

    }
}