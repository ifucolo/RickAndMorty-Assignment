package com.ifucolo.rickandmorty.ui.common.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.buttons.PrimaryButton
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText
import com.ifucolo.rickandmorty.ui.common.components.texts.TitleLargeText
import com.ifucolo.rickandmorty.ui.common.components.topbar.AppTopBar
import com.ifucolo.rickandmorty.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorStateScreen(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_message),
    subTitle: String = stringResource(R.string.try_again_later_message),
    retryText: String = stringResource(R.string.retryText),
    onBackPressed: (() -> Unit)? = null,
    errorMessage: String? = null,
    onRetry: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(Dimensions.paddingMedium)
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            onBackPressed?.let {
                AppTopBar(
                    onBackClick = onBackPressed
                )
            }
        }
    ){ padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleLargeText(
                modifier = Modifier
                    .padding(top = Dimensions.paddingMedium),
                text = title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
            BodyMediumText(
                modifier = Modifier
                    .padding(top = Dimensions.paddingSmall),
                text = subTitle,
                textAlign = TextAlign.Center
            )
            errorMessage?.let {
                BodyMediumText(
                    modifier = Modifier
                        .padding(top = Dimensions.paddingSmall),
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }
            onRetry?.let {
                PrimaryButton(
                    modifier = Modifier
                        .padding(top = Dimensions.paddingLarge),
                    text = retryText,
                    onClick = onRetry
                )
            }
        }
    }
}