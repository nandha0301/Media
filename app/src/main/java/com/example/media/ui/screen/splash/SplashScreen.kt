package com.example.media.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.media.R
import com.example.media.ui.viewModel.AuthViewModel
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.media.pref.AppPreference
import com.example.media.ui.screen.login.LoginObject
import com.example.media.ui.screen.media.mediaGallery.MediaGalleryObject
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashScreen(navController: NavController) {

    fun navigateToMediaGallery(){
        navController.navigate(MediaGalleryObject)
    }

    fun navigateToLogin(){
        navController.navigate(LoginObject)
    }
    val context = LocalContext.current
    val appPreference = AppPreference(context)



    LaunchedEffect(Unit) {
     val loginBoolean = appPreference.getLoginBoolean()
        if (loginBoolean ){
            delay(1200)
            navigateToMediaGallery()
        }else{
            delay(1200)
            navigateToLogin()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(model = null,
            contentDescription = null,
            loading = placeholder(R.drawable.onecare_app_intro_animation),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
    }

}