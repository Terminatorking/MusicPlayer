package ghazimoradi.soheil.musicplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ghazimoradi.soheil.musicplayer.data.model.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDataBase : RoomDatabase() {
    abstract fun dao(): Dao
}