package com.ifucolo.rickandmorty.ui.common.components.topbar.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppBarActions(
    open val imageVector: ImageVector,
    open val onActionClick: () -> Unit
) {
    class Share(imageVector: ImageVector = Icons.Filled.Share, onActionClick: () -> Unit) : AppBarActions(imageVector, onActionClick)
}
