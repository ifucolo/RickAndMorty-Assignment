package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.buttons.PrimaryButton
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText
import com.ifucolo.rickandmorty.ui.theme.Dimensions

@Composable
fun ListRetryItem(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BodyMediumText(
                text = stringResource(R.string.error_message),
            )
            PrimaryButton(
                onClick = onRetry,
                text = stringResource(R.string.retryText)
            )
        }
    }
}