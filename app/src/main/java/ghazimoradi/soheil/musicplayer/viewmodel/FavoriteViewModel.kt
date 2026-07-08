package ghazimoradi.soheil.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.musicplayer.data.database.Dao
import ghazimoradi.soheil.musicplayer.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val dao: Dao
) : ViewModel() {

    fun save(entity: Song) {
        viewModelScope.launch {
            dao.saveSongToFavorites(entity)
        }
    }

    fun delete(entity: Song) {
        viewModelScope.launch {
            dao.deleteSongFromFavorites(entity)
        }
    }

    suspend fun getFavorites(): Flow<List<Song>> {
        return withContext(Dispatchers.IO) {
            dao.getFavoriteSongs()
        }
    }

    suspend fun isExists(id: Long): Flow<Boolean> {
        return withContext(Dispatchers.IO) {
            dao.isSongExitsInFavorite(id)
        }
    }
}