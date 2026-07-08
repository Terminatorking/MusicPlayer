package ghazimoradi.soheil.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.utils.SongUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songUtils: SongUtils
) : ViewModel() {
    suspend fun getSongs(): List<Song> {
        return withContext(Dispatchers.IO) {
            songUtils.getSongs()
        }
    }
}