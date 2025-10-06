package com.ifucolo.rickandmorty.ui.common.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ifucolo.rickandmorty.ui.common.components.texts.TitleLargeText
import com.ifucolo.rickandmorty.ui.common.components.topbar.actions.AppBarActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    actions: List<AppBarActions> = emptyList(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: (() -> Unit)? = null,
    appBarCloseIconType: AppBarCloseIconType = AppBarCloseIconType.BACK
) {
    val navigationIcon = when(appBarCloseIconType) {
        AppBarCloseIconType.BACK -> Icons.AutoMirrored.Filled.ArrowBack
        AppBarCloseIconType.CLOSE -> Icons.Filled.Close
    }
    TopAppBar(
        modifier = modifier,
        title = {
            TitleLargeText(
                text = title,
                maxLines = 1
            )
        },
        scrollBehavior = scrollBehavior,

        navigationIcon = {
            onBackClick?.let {
                IconButton(
                    onClick = {
                        onBackClick.invoke()
                    }
                ) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            actions.forEach { action ->
                IconButton(
                    onClick = {
                        action.onActionClick()
                    }
                ) {
                    Icon(
                        imageVector = action.imageVector,
                        contentDescription = action.toString()
                    )
                }
            }
        }
    )
}

enum class AppBarCloseIconType {
    BACK, CLOSE
}