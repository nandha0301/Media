package com.example.media.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.media.model.MediaFileUpload
import com.example.media.ui.constant.FunctionConstants.Companion.gradientBrush

@Composable
fun CustomExpandableFAB(
    modifier: Modifier = Modifier,
    items: List<MediaFileUpload>,
    onItemClick: (MediaFileUpload) -> Unit) {

    var buttonClicked by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.End) {


            AnimatedVisibility(visible = buttonClicked,
                enter = expandVertically(tween(300)) + fadeIn(),
                exit = shrinkVertically(tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 10.dp)
                        .shadow(elevation = 5.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Card(shape = RoundedCornerShape(10.dp)) {
                        items.forEach { item ->

                            Box(
                                modifier = Modifier.background(brush = gradientBrush)
                                    .width(130.dp)) {

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            onItemClick(item)
                                            buttonClicked = false
                                        }
                                        .padding(10.dp)) {
                                    Image(imageVector = item.icon,
                                        contentDescription = item.text,
                                        colorFilter = ColorFilter.tint(Color.White),
                                        modifier = Modifier.size(22.dp),
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Text(
                                        text = item.text,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }

                            }
                        }
                    }
                }
            }


            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(gradientBrush)
                    .clickable { buttonClicked = !buttonClicked },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "FAB Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}