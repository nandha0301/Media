package com.example.media.ui.screen.media.mediaVideo

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.media.model.MediaVideo
import com.example.media.ui.constant.FunctionConstants
import com.example.media.ui.constant.FunctionConstants.Companion.byteArrayToBitmap
import com.example.media.ui.dialog.extractVideoThumbnail
import com.example.media.ui.theme.MediaBackgroundBtoW
import com.example.media.ui.viewModel.MediaViewModel

@Composable
fun MediaVideoScreen( navigateToMediaVideoDetailScreen : (MediaVideo) -> Unit) {
    MediaVideoScreenPreview( navigateToMediaVideoDetailScreen )
}


@Composable
fun MediaVideoScreenPreview( navigateToMediaVideoDetailScreen : (MediaVideo) -> Unit,
    viewModel: MediaViewModel = hiltViewModel()){

    val videos by viewModel.videos.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW)) {

            LazyVerticalGrid(columns = GridCells.Fixed(2),
                modifier = Modifier.padding(10.dp)
                    .padding(innerPadding).background(color = Color.Transparent),
                contentPadding = PaddingValues(bottom = 10.dp)
            ){

                items(videos.size) {index ->
                    VideoCardView(videos[index],navigateToMediaVideoDetailScreen)


                }

            }
        }


        }

}

fun extractVideoThumbnailNew(context: Context, uri: Uri): Bitmap? {
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

@Composable
fun VideoCardView(
    video: MediaVideo,
    navigateToMediaVideoDetailScreen: (MediaVideo) -> Unit
) {
    val thumbnailBitmap = video.thumbnail?.let { byteArrayToBitmap(it) }

    Card(
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navigateToMediaVideoDetailScreen(video) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // **Video Thumbnail with Play Button**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // ✅ Maintains proper video ratio
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (thumbnailBitmap != null) {
                    Image(
                        bitmap = thumbnailBitmap.asImageBitmap(),
                        contentDescription = "Video Thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Default Thumbnail",
                            modifier = Modifier.size(50.dp),
                            tint = Color.DarkGray
                        )
                    }
                }

                // **🎬 Centered Play Button**
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = "Play Video",
                    modifier = Modifier
                        .size(35.dp)
                        .shadow(8.dp, CircleShape) // Gives it a slight elevation
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .padding(8.dp),
                    tint = Color.White
                )
            }

            // **Video Name Section**
            Text(
                text = video.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}



/*@Composable
fun VideoCardView(video : MediaVideo,navigateToMediaVideoDetailScreen :(MediaVideo) -> Unit){
    val thumbnailBitmap = video.thumbnail?.let { byteArrayToBitmap(it) } // Load stored thumbnail


    Card(elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
            .height(140.dp)
            .padding(10.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null) {navigateToMediaVideoDetailScreen(video)},
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.MediaBackgroundBtoW)) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = video.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (thumbnailBitmap != null) {
                Image(
                    bitmap = thumbnailBitmap.asImageBitmap(),
                    contentDescription = "Video Thumbnail",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(imageVector = Icons.Default.VideoLibrary,
                    contentDescription = "Default Thumbnail",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray)
            }

        }


        }

}*/

@PreviewLightDark
@Composable
fun MediaVideoPreviewScreen(){
    MediaVideoScreenPreview(navigateToMediaVideoDetailScreen = {})

}