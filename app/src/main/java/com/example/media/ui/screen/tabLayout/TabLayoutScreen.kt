package com.example.media.ui.screen.tabLayout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Tab
import androidx.compose.ui.Alignment
import com.example.media.model.MediaVideo
import com.example.media.ui.screen.media.mediaImage.MediaImageScreen
import com.example.media.ui.screen.media.mediaVideo.MediaVideoScreen
import com.example.media.ui.theme.MediaBackgroundBtoW


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabLayoutScreen(navigateToMediaDetailScreen : (Int) -> Unit,
                    navigateToMediaVideoDetailScreen : (MediaVideo) -> Unit) {
    val tabHeadings = listOf(
        "Images" to Icons.Default.Image,
        "Videos" to Icons.Default.VideoLibrary
    )

    val pagerState = rememberPagerState(initialPage = 0) {
        tabHeadings.size
    }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.MediaBackgroundBtoW)) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabHeadings.forEachIndexed { index, (title, icon) ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)

                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = if (pagerState.currentPage == index) Color.Blue else Color.Gray
                        )
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index) Color.Blue else Color.Gray
                        )
                    }
                }
            }
        }


        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.MediaBackgroundBtoW)  ) { index ->
            when (index) {
                0 -> MediaImageScreen(navigateToMediaDetailScreen)
                1 -> MediaVideoScreen( navigateToMediaVideoDetailScreen)
            }
        }
    }
}




