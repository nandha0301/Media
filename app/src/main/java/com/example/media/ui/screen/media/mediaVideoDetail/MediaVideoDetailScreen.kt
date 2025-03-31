package com.example.media.ui.screen.media.mediaVideoDetail

import android.net.Uri
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.media.model.MediaImage
import com.example.media.model.MediaVideo
import com.example.media.ui.constant.FunctionConstants.Companion.extractDate
import com.example.media.ui.constant.FunctionConstants.Companion.extractTime
import com.example.media.ui.constant.FunctionConstants.Companion.extractVideoDuration
import com.example.media.ui.viewModel.MediaViewModel






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaVideoDetailScreen(
    id: Int,
    navController: NavController,
    viewModel: MediaViewModel = hiltViewModel()
) {

    fun navigateBack(){
        navController.popBackStack()
    }



    val video by produceState<MediaVideo?>(initialValue = null) {
        value = viewModel.getVideoById(id)
    }

    var videoDuration by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    video?.let { mediaVideo ->
        val context = LocalContext.current
        val uri = Uri.parse(mediaVideo.videoUri)
        videoDuration = extractVideoDuration(context, uri)

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Video Details",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // 🎥 Full-Screen Video Player
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Adjust height dynamically based on screen
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(uri)

                                val mediaController = MediaController(ctx)
                                mediaController.setAnchorView(this)
                                setMediaController(mediaController)

                                setOnPreparedListener { mediaPlayer ->
                                    mediaPlayer.setOnVideoSizeChangedListener { _, _, _ ->
                                        mediaController.setAnchorView(this)
                                    }
                                    start() // Auto-start when loaded
                                    isPlaying = true
                                }

                                setOnClickListener {
                                    if (isPlaying) {
                                        pause()
                                    } else {
                                        start()
                                    }
                                    isPlaying = !isPlaying
                                }

                                setOnErrorListener { _, _, _ ->
                                    Toast.makeText(ctx, "Cannot play this video", Toast.LENGTH_SHORT).show()
                                    true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // 📄 Video Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = mediaVideo.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                        Row {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Date: ${extractDate(mediaVideo.createdAt)}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row {
                            Icon(Icons.Default.AccessTime, contentDescription = "Time", tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Time: ${extractTime(mediaVideo.createdAt)}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row {
                            Icon(Icons.Default.Timer, contentDescription = "Duration", tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Duration: $videoDuration",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}






@PreviewLightDark
@Composable
fun MediaVideoDetailPreviewScreen(){
   // MediaVideoDetailScreenPreview("")


}