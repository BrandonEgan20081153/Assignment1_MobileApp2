package ie.wit.fantasyeplleague.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class eplModels(   var id: Long = 0,
                        var name: String = "",
                        var position: String = "",
                        var image: Uri = Uri.EMPTY,
                        var lat : Double = 0.0,
                        var lng: Double = 0.0,
                        var zoom: Float = 0f) : Parcelable