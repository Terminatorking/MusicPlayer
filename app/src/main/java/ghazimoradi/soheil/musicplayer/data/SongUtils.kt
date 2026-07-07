package ghazimoradi.soheil.musicplayer.data

import android.content.Context
import android.provider.MediaStore.Audio.Media

fun getSongs(context: Context): List<Song> {
    val songs = mutableListOf<Song>()

    val uri = Media.EXTERNAL_CONTENT_URI
    val selection = "${Media.IS_MUSIC}!=0"
    val sortOrder = "${Media.TITLE} ASC"

    val projection = arrayOf(
        Media._ID,
        Media.TITLE,
        Media.ARTIST,
        Media.DATA,
        Media.ALBUM_ID,
    )

    val cursor = context.contentResolver.query(
        uri, projection, selection, null, sortOrder
    )

    cursor?.use {
        val idCol = it.getColumnIndexOrThrow(Media._ID)
        val titleCol = it.getColumnIndexOrThrow(Media.TITLE)
        val artistCol = it.getColumnIndexOrThrow(Media.ARTIST)
        val dataCol = it.getColumnIndexOrThrow(Media.DATA)
        val albumCol = it.getColumnIndexOrThrow(Media.ALBUM_ID)

        while (it.moveToNext()) {
            val id = it.getLong(idCol)
            val title = it.getString(titleCol)
            val artist = it.getString(artistCol)
            val data = it.getString(dataCol)
            val albumId = it.getLong(albumCol)

            songs.add(Song(id, title, artist, data, albumId))
        }
    }

    return songs
}