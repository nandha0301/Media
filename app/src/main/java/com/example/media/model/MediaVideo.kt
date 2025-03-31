package com.example.media.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_videos")
data class MediaVideo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val videoUri: String,
    val thumbnail: ByteArray?,
    val createdAt : String,

)