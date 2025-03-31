package com.example.media.ui.dialog

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.media.R
import com.example.media.ui.constant.FunctionConstants
import com.example.media.ui.viewModel.MediaViewModel


@Composable
fun AddImageDialog(onCancel: () -> Unit,
                   viewModel: MediaViewModel = hiltViewModel()) {

    val context = LocalContext.current
    var imageName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var sizeInMb by remember { mutableDoubleStateOf(0.0) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val imageSizeInBytes = FunctionConstants.getImageSizeInBytes(context.contentResolver, it)
             sizeInMb = imageSizeInBytes / (1024.0 * 1024.0)

            if (sizeInMb <= 5) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "Image size should be less than 5 MB", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun uploadImage(){
        if (selectedImageUri != null && imageName.text.isNotEmpty()) {
            viewModel.saveImageToRoom(context, selectedImageUri!!, imageName.text,
                imageSize = sizeInMb, createdDate = FunctionConstants.timestampNow())

            onCancel()
        } else {
            Toast.makeText(context, "Enter a name and select an image", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {

                    Text(text = "Add Image",
                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center)


                    Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append("Name ")
                            }
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append("*")
                            }
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(value = imageName,
                        onValueChange = { imageName = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append("Upload Image ")
                            }
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append("*")
                            }
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val painter: Painter = if (selectedImageUri != null) {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(selectedImageUri)
                                .diskCachePolicy(CachePolicy.DISABLED)
                                .build()
                        )
                    } else {
                        painterResource(id = R.drawable.images)
                    }

                    Box(modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Transparent)
                            .clickable(interactionSource = remember { MutableInteractionSource() },
                                indication = null) {
                                galleryLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center) {
                        Image(
                            painter = painter,
                            contentDescription = "Upload Image",
                            modifier = Modifier.size(150.dp)
                                .background(color = Color.Transparent),
                            contentScale = ContentScale.Fit
                        )
                    }



                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        TextButton(onClick = onCancel) {
                            Text(text = "Cancel",)
                        }

                        Button(onClick = {
                            uploadImage()
                        }) {
                            Text(text = "Upload")
                        }
                    }
                }
            }
        }
    }
}




