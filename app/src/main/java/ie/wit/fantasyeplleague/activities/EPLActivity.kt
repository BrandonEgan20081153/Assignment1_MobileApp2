package ie.wit.fantasyeplleague.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.fantasyeplleague.R
import ie.wit.fantasyeplleague.databinding.ActivityEplBinding
import ie.wit.fantasyeplleague.helpers.showImagePicker
import ie.wit.fantasyeplleague.main.MainApp
import ie.wit.fantasyeplleague.models.Location
import ie.wit.fantasyeplleague.models.eplModels
import timber.log.Timber
import timber.log.Timber.i

class EPLActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEplBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var player = eplModels()
    var edit = false
    lateinit var app: MainApp
    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEplBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        binding.playerLocation.setOnClickListener {
            i ("Set Location Pressed")
        }
        binding.playerLocation.setOnClickListener {
            val location = Location(53.463493, -2.292279, 15f)
            if (player.zoom != 0f) {
                location.lat =  player.lat
                location.lng = player.lng
                location.zoom = player.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }


        if (intent.hasExtra("player_edit")) {
            edit = true
            player = intent.extras?.getParcelable("player_edit")!!
            binding.playerName.setText(player.name)
            binding.position.setText(player.position)
            binding.btnAdd.setText(R.string.save_player)
            binding.chooseImage.setOnClickListener {
                showImagePicker(imageIntentLauncher)
               }
            Picasso.get()
                .load(player.image)
                .into(binding.playerImage)
            if (player.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_player_image)
            }

        }


        i("EPL Activity started...")
        binding.btnAdd.setOnClickListener() {
            player.name = binding.playerName.text.toString()
            player.position = binding.position.text.toString()
            if (player.name.isEmpty()) {
                Snackbar.make(it,R.string.enter_player_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.players.update(player.copy())
                } else {
                    app.players.create(player.copy())
                }
            }
            i("add Button Pressed: $player")
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            player.image = result.data!!.data!!
                            Picasso.get()
                                .load(player.image)
                                .into(binding.playerImage)
                            binding.chooseImage.setText(R.string.change_player_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_player, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.players.delete(player)
                finish()
            }
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            player.lat = location.lat
                            player.lng = location.lng
                            player.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }



}