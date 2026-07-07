package ghazimoradi.soheil.musicplayer.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ghazimoradi.soheil.musicplayer.data.Song

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onSongClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(songs) { index, song ->
            SongListItem(
                song = song,
                onClick = {
                    onSongClick.invoke(index)
                }
            )
        }
    }
}