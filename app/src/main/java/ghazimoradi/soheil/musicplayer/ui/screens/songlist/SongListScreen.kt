package ghazimoradi.soheil.musicplayer.ui.screens.songlist

import android.Manifest.permission
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ghazimoradi.soheil.musicplayer.R
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.ui.components.ProjectOutlinedTextField
import ghazimoradi.soheil.musicplayer.ui.components.SongList
import ghazimoradi.soheil.musicplayer.ui.screens.FavoriteScreen
import ghazimoradi.soheil.musicplayer.ui.theme.Bayside
import ghazimoradi.soheil.musicplayer.ui.theme.Transparent
import ghazimoradi.soheil.musicplayer.ui.theme.White
import ghazimoradi.soheil.musicplayer.viewmodel.SongViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SongListScreen(
    padding: PaddingValues,
    viewmodel: SongViewModel = hiltViewModel(),
    navigateToPlayer: (songs: List<Song>, position: Int) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    var search by remember { mutableStateOf("") }

    val tabs = listOf("Songs", "Favorites")

    val allSongs = remember {
        mutableStateOf<List<Song>>(emptyList())
    }

    val songsState by remember {
        derivedStateOf {
            if (search.isEmpty()) {
                allSongs.value
            } else {
                allSongs.value.filter { it.title?.contains(search, ignoreCase = true) == true }
            }
        }
    }

    val permission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission.READ_MEDIA_AUDIO
        } else {
            permission.READ_EXTERNAL_STORAGE
        }
    }

    val shouldShowPermissionLay = remember { mutableStateOf(false) }

    val permissionState = rememberPermissionState(permission) {
        shouldShowPermissionLay.value = !it
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            val songs = viewmodel.getSongs()
            allSongs.value = songs
        }
    }

    val shouldShowDialog = remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxSize()) {
            if (!permissionState.status.isGranted) {
                if (shouldShowDialog.value) {
                    ShowPermissionDialog {
                        shouldShowDialog.value = false
                        shouldShowPermissionLay.value = true
                        permissionState.launchPermissionRequest()
                    }
                }

                if (shouldShowPermissionLay.value) {
                    StoragePermissionLay()
                }

            } else {
                Spacer(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding(), bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                SecondaryTabRow(
                    selectedTabIndex = tabIndex,
                    modifier = Modifier.padding(20.dp),
                    containerColor = Transparent,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            color = Bayside,
                            modifier = Modifier.tabIndicatorOffset(
                                tabIndex,
                                matchContentSize = false
                            )
                        )
                    },
                    tabs = {
                        tabs.forEachIndexed { index, value ->
                            val isSelected = tabIndex == index

                            Tab(
                                text = {
                                    Text(
                                        text = value,
                                        color = if (isSelected) Bayside else White
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    tabIndex = index
                                }
                            )
                        }
                    }
                )

                if (tabIndex == 0) {

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

                } else {
                    FavoriteScreen(navigateToPlayer = navigateToPlayer)
                }
            }
        }
    }
}