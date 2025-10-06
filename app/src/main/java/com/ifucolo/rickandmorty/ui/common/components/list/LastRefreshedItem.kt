package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText
import com.ifucolo.rickandmorty.ui.theme.Dimensions

@Composable
fun LastRefreshedItem(
    text: String
) {
    val lastRefreshedText = stringResource(R.string.last_refreshed, text)

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.History, contentDescription = null)
        BodyMediumText(text = lastRefreshedText)
    }
}