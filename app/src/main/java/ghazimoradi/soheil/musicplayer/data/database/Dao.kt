package ghazimoradi.soheil.musicplayer.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.utils.TABLE_NAME
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion.REPLACE

@Dao
interface Dao {

    @Insert(onConflict = REPLACE)
    suspend fun saveSongToFavorites(entity: Song)

    @Delete
    suspend fun deleteSongFromFavorites(entity: Song)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFavoriteSongs(): Flow<List<Song>>

    @Query("SELECT EXISTS (SELECT 1 FROM $TABLE_NAME WHERE id = :id)")
    fun isSongExitsInFavorite(id: Long): Flow<Boolean>
}