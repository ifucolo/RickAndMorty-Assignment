package com.ifucolo.rickandmorty.ui.common.components.image

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    imageRes: ImageResource,
    title: String,
    contentColor: Color = Color.Unspecified,
    context: Context = LocalContext.current
) {
    when (imageRes) {
        is ImageResource.Drawable -> {
            Image(
                painter = painterResource(id = imageRes.id),
                contentDescription = title,
                contentScale = ContentScale.Crop
            )
        }
        is ImageResource.Vector -> {
            Icon(
                imageVector = imageRes.vector,
                contentDescription = title,
                modifier = modifier,
                tint = contentColor
            )
        }

        is ImageResource.Url -> {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageRes.url)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
fun ImageComponentPreview() {
    RickAndMortyTheme {
        ImageComponent(
            modifier = Modifier.size(Dimensions.iconSizeLarge),
            imageRes = ImageResource.Drawable(com.ifucolo.rickandmorty.R.drawable.ic_launcher_background),
            title = "Preview Image"
        )
    }
}