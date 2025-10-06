package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyLargeText

@Composable
fun EmptyItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BodyLargeText(
            text = stringResource(R.string.empty_characters),
            textAlign = TextAlign.Center
        )
    }
}