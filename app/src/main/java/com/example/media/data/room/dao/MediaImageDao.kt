package com.example.media.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.media.model.MediaImage

@Dao
interface MediaImageDao {
    @Insert
    suspend fun insertImage(image: MediaImage)

    @Query("SELECT * FROM media_images")
    suspend fun getAllImages(): List<MediaImage>

    @Query("SELECT * FROM media_images WHERE id = :id LIMIT 1")
    suspend fun getImageById(id: Int): MediaImage?
}