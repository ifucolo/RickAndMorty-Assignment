package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText

@Composable
fun EndOfTheListItem() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BodyMediumText(
            text = stringResource(R.string.endOfTheList),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}