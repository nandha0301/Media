package com.example.media.ui.screen.media.mediaImage

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.media.R
import com.example.media.model.MediaImage
import com.example.media.ui.theme.MediaBackgroundBtoW
import com.example.media.ui.theme.oneCareTextWtoB
import com.example.media.ui.viewModel.MediaViewModel

@Composable
fun MediaImageScreen(navigateToMediaDetailScreen : (Int) -> Unit) {

    MediaImageScreenPreview(navigateToMediaDetailScreen)
}

@Composable
fun MediaImageScreenPreview(navigateToMediaDetailScreen : (Int) -> Unit,
                            viewModel: MediaViewModel = hiltViewModel()
){
    val images by viewModel.images.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW)) {

            LazyVerticalGrid(columns = GridCells.Fixed(2),
                modifier = Modifier.padding(10.dp)
                .padding(innerPadding).background(color = Color.Transparent),
                contentPadding = PaddingValues(bottom = 10.dp)){

                items(images.size) {index ->
                    MediaCardView(mediaImage = images[index],
                        navigateToMediaDetailScreen)
                }

            }

        }


    }
}

@Composable
fun MediaCardView(mediaImage: MediaImage, navigateToMediaDetailScreen : (Int) -> Unit){

    val bitmap = BitmapFactory.decodeByteArray(mediaImage.imageData, 0, mediaImage.imageData.size)


    Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp)
                .clickable {
                    navigateToMediaDetailScreen(mediaImage.id)
                },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // ✅ Makes the image take most of the space
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(bitmap),
                        contentDescription = mediaImage.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)), // ✅ Smooth corners
                        contentScale = ContentScale.Crop // ✅ Properly fit image
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mediaImage.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // ✅ Prevents text overflow issues
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    /*Card(elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
            .height(140.dp)
            .padding(10.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null) {navigateToMediaDetailScreen (mediaImage.id)},
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.MediaBackgroundBtoW)) {
        Column(modifier = Modifier.padding(5.dp)) {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Image(painter = rememberAsyncImagePainter(bitmap),
                    contentDescription = mediaImage.name,
                    modifier = Modifier.width(80.dp)
                        .height(80.dp).padding(10.dp),
                    contentScale = ContentScale.Crop)
            }
            Text(mediaImage.name, modifier = Modifier.fillMaxWidth(),
                maxLines = 2,fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center)


        }
    }*/

}


@PreviewLightDark
@Composable
fun MediaImagePreviewScreen(){
    MediaImageScreenPreview(navigateToMediaDetailScreen = {})
}