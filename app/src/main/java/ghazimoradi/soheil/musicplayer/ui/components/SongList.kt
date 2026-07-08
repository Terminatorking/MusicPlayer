package ghazimoradi.soheil.musicplayer.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ghazimoradi.soheil.musicplayer.data.model.Song

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onSongClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            items = songs,
            key = { _, song -> song.id }
        ) { index, song ->
            SongListItem(
                song = song,
                onClick = {
                    onSongClick.invoke(index)
                }
            )
        }
    }
}