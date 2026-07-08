package ghazimoradi.soheil.musicplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.ui.components.SongList
import ghazimoradi.soheil.musicplayer.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    viewmodel: FavoriteViewModel = hiltViewModel(),
    navigateToPlayer: (songs: List<Song>, position: Int) -> Unit
) {

    val favoriteSongs = remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewmodel.getFavorites().collect {
            favoriteSongs.value = it
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SongList(
            songs = favoriteSongs.value,
            onSongClick = { pos ->
                navigateToPlayer.invoke(favoriteSongs.value, pos)
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
        )
    }
}