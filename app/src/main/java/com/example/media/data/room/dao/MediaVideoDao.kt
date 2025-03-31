package com.example.media.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.media.model.MediaImage
import com.example.media.model.MediaVideo
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: MediaVideo)

    @Query("SELECT * FROM media_videos")
    fun getAllVideos(): Flow<List<MediaVideo>>

    @Query("SELECT * FROM media_videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: Int): MediaVideo?
}


