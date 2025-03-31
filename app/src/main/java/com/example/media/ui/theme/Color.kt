package com.example.media.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Pink = Color(0xFF1D6AFF)
val Blue = Color(0xFFF97DB3)


val ColorScheme.MediaBackgroundBtoW
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black
    else Color.White

val ColorScheme.onecareCard
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xBF34343B)
    else Color.White

val ColorScheme.oneCareTextWtoB
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White
    else Color.Black