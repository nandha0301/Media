package com.example.media.ui.dialog

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.media.ui.constant.FunctionConstants
import com.example.media.ui.viewModel.MediaViewModel




@Composable
fun AddVideoDialog(
    onCancel: () -> Unit,
    viewModel: MediaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var videoName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var videoThumbnail by remember { mutableStateOf<Bitmap?>(null) }

    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val videoSizeInBytes = FunctionConstants.getImageSizeInBytes(context.contentResolver, it)
            val sizeInMb = videoSizeInBytes / (1024.0 * 1024.0)

            if (sizeInMb <= 15) {
                selectedVideoUri = uri
                videoThumbnail = extractVideoThumbnail(context, uri)
            } else {
                Toast.makeText(context, "Video size should be less than 15 MB", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadVideo() {
        if (selectedVideoUri != null && videoName.text.isNotEmpty()) {
            viewModel.saveVideoToRoom(
                context, selectedVideoUri!!, videoName.text,
                createdDate = FunctionConstants.timestampNow()
            )
            onCancel()
        } else {
            Toast.makeText(context, "Please enter a name and select a video", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add Video",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                // 📝 Video Name Input
                Column {
                    Text(
                        text = "Video Name *",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = videoName,
                        onValueChange = { videoName = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }

                // 🎥 Video Upload Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Upload Video *",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )

                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .clickable { videoLauncher.launch("video/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (videoThumbnail != null) {
                            Image(
                                painter = BitmapPainter(videoThumbnail!!.asImageBitmap()),
                                contentDescription = "Selected Video",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.VideoLibrary,
                                    contentDescription = "Select Video",
                                    modifier = Modifier.size(80.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Tap to select video",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // 🛠 Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onCancel) {
                        Text(text = "Cancel", color = MaterialTheme.colorScheme.primary)
                    }

                    Button(
                        onClick = { uploadVideo() },
                        enabled = videoName.text.isNotEmpty() && selectedVideoUri != null
                    ) {
                        Text(text = "Upload")
                    }
                }
            }
        }
    }
}


fun extractVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val frame = retriever.getFrameAtTime(1000000) // Get frame at 1 second
        retriever.release()
        frame
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}