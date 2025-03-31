package com.example.media.di

import android.app.Application
import androidx.room.Room
import com.example.media.data.MediaDatabase
import com.example.media.ui.constant.FunctionConstants.Companion.ROOM_DB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(application: Application): MediaDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            MediaDatabase::class.java,
            ROOM_DB)
            .fallbackToDestructiveMigration(false)
            .allowMainThreadQueries()
            .build()


    @Provides
    fun provideMediaImageRoomDao(mediaDatabase: MediaDatabase) = mediaDatabase.mediaImageDao()

    @Provides
    fun provideMediaVideoRoomDao(mediaDatabase: MediaDatabase) = mediaDatabase.mediaVideoDao()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}
