package com.zhdanon.rickandmortycompose.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.zhdanon.rickandmortycompose.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GlideImageWithPreview(
    data: String,
    modifier: Modifier = Modifier
        .size(60.dp)
        .clip(CircleShape),
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    if (data.isEmpty()) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alignment = Alignment.Center
        )
    } else {
        Image(
            painter = rememberImagePainter(data),
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GlideImageWithPreviewPreview() {
    GlideImageWithPreview(data = "")
}