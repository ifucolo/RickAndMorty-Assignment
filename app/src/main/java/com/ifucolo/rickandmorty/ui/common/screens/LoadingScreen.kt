package com.ifucolo.rickandmorty.ui.common.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyLargeText
import com.ifucolo.rickandmorty.ui.theme.Dimensions

const val LOADING_SCREEN_TEST_TAG = "loading_screen_test_tag"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingStateScreen(
    modifier: Modifier = Modifier,
    message: String? = null,
    contentPadding: PaddingValues = PaddingValues(Dimensions .paddingMedium),
    showProgressIndicator: Boolean = true
) {
    Scaffold(
        modifier = modifier.testTag(LOADING_SCREEN_TEST_TAG).fillMaxSize()
    ){ padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showProgressIndicator) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(bottom = Dimensions.paddingMedium)
                )
            }

            BodyLargeText(
                text = message?: stringResource(R.string.loadingMessage),
                textAlign = TextAlign.Center
            )
        }
    }

}