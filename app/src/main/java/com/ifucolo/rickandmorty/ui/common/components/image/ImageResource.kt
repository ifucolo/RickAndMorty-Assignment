package com.ifucolo.rickandmorty.ui.common.components.image

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ImageResource {
    data class Url(val url: String, @DrawableRes val fallbackId: Int? = null) : ImageResource()
    data class Drawable(@DrawableRes val id: Int) : ImageResource()
    data class Vector(val vector: ImageVector) : ImageResource()
}