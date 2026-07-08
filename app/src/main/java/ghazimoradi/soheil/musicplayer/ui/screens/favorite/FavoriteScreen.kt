package ghazimoradi.soheil.musicplayer.ui.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.ui.components.ProjectOutlinedTextField
import ghazimoradi.soheil.musicplayer.ui.components.SongList
import ghazimoradi.soheil.musicplayer.ui.theme.White
import ghazimoradi.soheil.musicplayer.viewmodel.FavoriteViewModel
import kotlin.text.isEmpty

@Composable
fun FavoriteScreen(
    viewmodel: FavoriteViewModel = hiltViewModel(),
    navigateToPlayer: (songs: List<Song>, position: Int) -> Unit
) {
    var search by remember { mutableStateOf("") }

    val favoriteSongs = remember { mutableStateOf<List<Song>>(emptyList()) }

    val songsState by remember {
        derivedStateOf {
            if (search.isEmpty()) {
                favoriteSongs.value
            } else {
                favoriteSongs.value.filter { it.title?.contains(search, ignoreCase = true) == true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.getFavorites().collect {
            favoriteSongs.value = it
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        if (favoriteSongs.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Favorite list is Empty",
                    color = White,
                )
            }
        } else {

            ProjectOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                value = search,
                onValueChange = {
                    search = it
                }
            )

            SongList(
                songs = songsState,
                onSongClick = { pos ->
                    navigateToPlayer.invoke(songsState, pos)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            )
        }
    }
}