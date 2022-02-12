package ie.wit.fantasyeplleague.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.fantasyeplleague.databinding.CardPlayerBinding
import ie.wit.fantasyeplleague.models.eplModels
import timber.log.Timber.i

interface PlayerListener {
    fun onPlayerClick(player: eplModels)
}

class PlayerAdapter constructor(private var players: List<eplModels>,
                                   private val listener: PlayerListener) :
    RecyclerView.Adapter<PlayerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlayerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val player = players[holder.adapterPosition]
        holder.bind(player, listener)
    }

    override fun getItemCount(): Int = players.size

    class MainHolder(private val binding : CardPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: eplModels, listener: PlayerListener) {
            binding.playerName.text = player.name
            binding.position.text = player.position
            Picasso.get().load(player.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPlayerClick(player) }
            }
    }
}