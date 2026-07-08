package ghazimoradi.soheil.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String?,
    val artist: String?,
    /**
     * data is the path of the song
     */
    val data: String,
    val albumId: Long,
)