package ie.wit.fantasyeplleague.activities

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.fantasyeplleague.R
import ie.wit.fantasyeplleague.adapters.PlayerAdapter
import ie.wit.fantasyeplleague.adapters.PlayerListener
import ie.wit.fantasyeplleague.databinding.ActivityEplBinding
import ie.wit.fantasyeplleague.databinding.ActivityPlayerListBinding
import ie.wit.fantasyeplleague.databinding.CardPlayerBinding
import ie.wit.fantasyeplleague.main.MainApp
import ie.wit.fantasyeplleague.models.eplModels

class PlayerListActivity : AppCompatActivity(), PlayerListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPlayerListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadPlayers()

        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, EPLActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, EPLMapActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPlayerClick(player: eplModels) {
        val launcherIntent = Intent(this, EPLActivity::class.java)
        launcherIntent.putExtra("player_edit", player)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadPlayers() }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun loadPlayers() {
        showPlayers(app.players.findAll())
    }

    fun showPlayers (players: List<eplModels>) {
        binding.recyclerView.adapter = PlayerAdapter(players, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}