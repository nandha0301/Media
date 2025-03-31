package com.example.media.ui.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.data.room.dao.MediaImageDao
import com.example.media.data.room.dao.MediaVideoDao
import com.example.media.model.MediaImage
import com.example.media.model.MediaVideo
import com.example.media.ui.constant.FunctionConstants.Companion.bitmapToByteArray
import com.example.media.ui.constant.FunctionConstants.Companion.saveVideoToInternalStorage
import com.example.media.ui.screen.media.mediaVideo.extractVideoThumbnailNew
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class MediaViewModel @Inject constructor(
    private val mediaImageDio: MediaImageDao,
    private val mediaVideoDio: MediaVideoDao) : ViewModel() {

    private val _images = MutableStateFlow<List<MediaImage>>(emptyList())
    val images: StateFlow<List<MediaImage>> = _images.asStateFlow()

    private val _videos = MutableStateFlow<List<MediaVideo>>(emptyList())
    val videos: StateFlow<List<MediaVideo>> = _videos.asStateFlow()




    init {
        fetchImages()

        fetchVideos()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            _images.value = mediaImageDio.getAllImages()
        }
    }

    fun saveImageToRoom(context: Context, uri: Uri, imageName: String,
                        createdDate : String,imageSize : Double) {
        viewModelScope.launch {
            val imageData = uriToByteArray(context, uri)
            if (imageData != null) {
                val mediaImage = MediaImage(name = imageName, imageData = imageData,
                  createdAt = createdDate, imageSize = imageSize  )
                mediaImageDio.insertImage(mediaImage)
                fetchImages()
            }
        }
    }

    private fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getImageById(id: Int): MediaImage? {
        return runBlocking { mediaImageDio.getImageById(id) }
    }


    fun getVideoById(id: Int): MediaVideo? {
        return runBlocking { mediaVideoDio.getVideoById(id) }
    }



    private fun fetchVideos() {
        viewModelScope.launch {
            mediaVideoDio.getAllVideos().collect { videoList ->
                _videos.value = videoList
            }
        }
    }

    fun saveVideoToRoom(context: Context, videoUri: Uri, videoName: String,
                        createdDate: String) {
        viewModelScope.launch {
            val savedUri = saveVideoToInternalStorage(context, videoUri, videoName)

            if (savedUri != null) {
                val thumbnailBitmap = extractVideoThumbnailNew(context, savedUri)
                val thumbnailByteArray = thumbnailBitmap?.let { bitmapToByteArray(it) }

                val mediaVideo = MediaVideo(
                    name = videoName,
                    videoUri = savedUri.toString(), // Save the new URI
                    thumbnail = thumbnailByteArray,
                    createdAt = createdDate
                )
                mediaVideoDio.insertVideo(mediaVideo)
            }
        }
    }





}
