package ghazimoradi.soheil.musicplayer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    /**
     * data is the path of the song
     */
    val data: String,
    val albumId: Long,
): Parcelable