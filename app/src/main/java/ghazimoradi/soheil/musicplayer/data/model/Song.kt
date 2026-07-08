package ghazimoradi.soheil.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ghazimoradi.soheil.musicplayer.utils.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Song(
    @PrimaryKey
    val id: Long,
    val title: String?,
    val artist: String?,
    /**
     * data is the path of the song
     */
    val data: String,
    val albumId: Long,
)