package com.example.media.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.media.ui.screen.login.LoginObject
import com.example.media.ui.screen.login.LoginScreen
import com.example.media.ui.screen.media.mediaImageDetail.MediaImageDetailObject
import com.example.media.ui.screen.media.mediaImageDetail.MediaImageDetailScreen
import com.example.media.ui.screen.media.mediaGallery.MediaGalleryObject
import com.example.media.ui.screen.media.mediaGallery.MediaGalleryScreen
import com.example.media.ui.screen.media.mediaVideoDetail.MediaVideoDetailObject
import com.example.media.ui.screen.media.mediaVideoDetail.MediaVideoDetailScreen
import com.example.media.ui.screen.signup.SignUpObject
import com.example.media.ui.screen.signup.SignUpScreen
import com.example.media.ui.screen.splash.SplashObject
import com.example.media.ui.screen.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashObject) {

        composable<SplashObject> { SplashScreen(navController)  }

        composable<LoginObject> {  LoginScreen(navController) }

        composable<SignUpObject> { SignUpScreen(navController) }

        composable<MediaGalleryObject> { MediaGalleryScreen(navController) }

        composable<MediaImageDetailObject> {
            val args = it.toRoute<MediaImageDetailObject>()
            MediaImageDetailScreen(id = args.id,
                navController) }

        composable<MediaVideoDetailObject> {
            val args = it.toRoute<MediaVideoDetailObject>()

               MediaVideoDetailScreen(id = args.id,navController)
        }
    }
}