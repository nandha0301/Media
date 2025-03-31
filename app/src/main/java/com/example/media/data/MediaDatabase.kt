package com.example.media.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.media.data.room.dao.MediaImageDao
import com.example.media.data.room.dao.MediaVideoDao
import com.example.media.model.MediaImage
import com.example.media.model.MediaVideo


@Database(
    entities = [
        MediaImage::class,
       MediaVideo ::class
],
    version = 5,
    exportSchema = false)

abstract class MediaDatabase : RoomDatabase() {

    abstract fun mediaImageDao(): MediaImageDao
    abstract fun mediaVideoDao() : MediaVideoDao

}