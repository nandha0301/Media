package com.example.media.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_images")
data class MediaImage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageSize:Double,
    val createdAt : String,
    val imageData: ByteArray
)
