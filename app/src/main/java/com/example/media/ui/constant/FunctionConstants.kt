package com.example.media.ui.constant

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.Brush
import com.example.media.ui.theme.Blue
import com.example.media.ui.theme.Pink
import okhttp3.internal.concurrent.formatDuration
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FunctionConstants {



        companion object {

            const val ROOM_DB = "media_database"


        fun ToastMessage(context : Context, content : String){
            Toast.makeText(context,content, Toast.LENGTH_SHORT).show()
        }

        fun isValidEmail(email: String): Boolean {
            return "@" in email && "." in email
        }

        val gradientBrush = Brush.verticalGradient(colors = listOf(Blue, Pink))

        val tabHeadings = listOf("Image" to Icons.Default.Image, "Video" to Icons.Default.VideoLibrary)


         fun getImageSizeInBytes(contentResolver: ContentResolver, uri: Uri): Long {
            var sizeInBytes: Long = 0
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    sizeInBytes = inputStream.available().toLong()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return sizeInBytes
        }

            fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                return stream.toByteArray()
            }

            fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }

            fun saveVideoToInternalStorage(context: Context, uri: Uri, fileName: String): Uri? {
                return try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val videoFile = File(context.filesDir, "$fileName.mp4")

                    FileOutputStream(videoFile).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                    inputStream?.close()

                    Uri.fromFile(videoFile) // Return the permanent URI
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            private fun extractVideoThumbnailNew(context: Context, uri: Uri): Bitmap? {
                return try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(context, uri)
                    val frame = retriever.getFrameAtTime(1000000) // Get a frame at 1 second
                    retriever.release()
                    frame
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun timestampNow(): String {
                val now = Instant.now()  // Get the current instant in time
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)
                val formattedNow = formatter.format(now)
                return formattedNow
            }

            fun extractDate(timestamp: String): String {
                return try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Parse as UTC

                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = inputFormat.parse(timestamp) // ✅ Convert String to Date
                    outputFormat.format(date!!) // ✅ Format Date
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Invalid Date" // Handle parsing error
                }
            }

            fun extractTime(timestamp: String): String {
                return try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                    val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    val date = inputFormat.parse(timestamp) // ✅ Convert String to Date
                    outputFormat.format(date!!) // ✅ Format Date
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Invalid Time"
                }
            }

            fun extractVideoDuration(context: Context, uri: Uri): String {
                return try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(context, uri)
                    val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                    retriever.release()

                    formatDuration(durationMs)
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Unknown"
                }
            }


        }


}