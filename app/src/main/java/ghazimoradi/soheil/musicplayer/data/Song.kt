package ghazimoradi.soheil.musicplayer.data

data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    /**
     * data is the path of the song
     */
    val data: String,
    val albumId: Long,
)