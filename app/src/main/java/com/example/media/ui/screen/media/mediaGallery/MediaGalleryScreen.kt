package com.example.media.ui.screen.media.mediaGallery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import com.example.media.R
import com.example.media.model.MediaFileUpload
import com.example.media.model.MediaVideo
import com.example.media.ui.common.CustomExpandableFAB
import com.example.media.ui.dialog.AddImageDialog
import com.example.media.ui.dialog.AddVideoDialog
import com.example.media.ui.screen.media.mediaImageDetail.MediaImageDetailObject
import com.example.media.ui.screen.media.mediaVideoDetail.MediaVideoDetailObject
import com.example.media.ui.screen.tabLayout.TabLayoutScreen
import com.example.media.ui.theme.MediaBackgroundBtoW

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MediaGalleryScreen(navController: NavController) {

    fun navigateToMediaDetailScreen(id : Int){
        navController.navigate(MediaImageDetailObject(id))
    }
    fun navigateToMediaVideoDetailScreen(mediaVideo: MediaVideo){
        navController.navigate(MediaVideoDetailObject(id = mediaVideo.id))
    }
    MediaGalleryScreenPreview(::navigateToMediaDetailScreen,::navigateToMediaVideoDetailScreen)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MediaGalleryScreenPreview(navigateToMediaDetailScreen : (Int) -> Unit,
                              navigateToMediaVideoDetailScreen : (MediaVideo) -> Unit) {
    var showAddImageCustomDialog by remember { mutableStateOf(false) }
    var showAddVideoCustomDialog by remember { mutableStateOf(false) }

    val uploadImage =stringResource(id = R.string.upload_image)
    val uploadVideo =  stringResource(id = R.string.upload_video)

    Scaffold(modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW),
        floatingActionButton = {

            val itemList = listOf(
                MediaFileUpload(icon = Icons.Default.Image, text =  uploadImage),
                MediaFileUpload(icon = Icons.Default.VideoLibrary, text = uploadVideo))

            CustomExpandableFAB(items = itemList,
                onItemClick = {item ->

                    when(item.text) {
                        uploadImage -> showAddImageCustomDialog = true
                        uploadVideo-> showAddVideoCustomDialog = true
                    }
                })
        }) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .background(color = MaterialTheme.colorScheme.MediaBackgroundBtoW)) {

            TabLayoutScreen(navigateToMediaDetailScreen,
                navigateToMediaVideoDetailScreen )

            if (showAddImageCustomDialog){
                AddImageDialog(onCancel = {showAddImageCustomDialog = false})
            }

            if (showAddVideoCustomDialog){
                AddVideoDialog(onCancel = {showAddVideoCustomDialog = false})
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewLightDark
@Composable
fun MediaGalleryPreviewScreen() {

   // MediaGalleryScreenPreview(navigateToMediaDetailScreen = {})
}