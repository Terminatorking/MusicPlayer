package ghazimoradi.soheil.musicplayer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.musicplayer.data.database.SongDataBase
import ghazimoradi.soheil.musicplayer.utils.DATA_BASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        SongDataBase::class.java,
        DATA_BASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    @Singleton
    fun provideDao(database: SongDataBase) = database.dao()
}