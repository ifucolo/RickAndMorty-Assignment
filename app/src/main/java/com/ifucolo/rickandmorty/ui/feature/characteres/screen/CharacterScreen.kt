package com.ifucolo.rickandmorty.ui.feature.characteres.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.domain.CharacterDetail
import com.ifucolo.rickandmorty.export.rememberCharacterJsonExportLauncher
import com.ifucolo.rickandmorty.ui.common.components.image.ImageComponent
import com.ifucolo.rickandmorty.ui.common.components.image.ImageResource
import com.ifucolo.rickandmorty.ui.common.components.snackbar.AppSnackbarHost
import com.ifucolo.rickandmorty.ui.common.components.snackbar.rememberSnackbarController
import com.ifucolo.rickandmorty.ui.common.components.topbar.AppTopBar
import com.ifucolo.rickandmorty.ui.common.components.topbar.actions.AppBarActions
import com.ifucolo.rickandmorty.ui.common.screens.ErrorStateScreen
import com.ifucolo.rickandmorty.ui.common.screens.LoadingStateScreen
import com.ifucolo.rickandmorty.ui.feature.characteres.components.CharacterCardInfoComponent
import com.ifucolo.rickandmorty.ui.feature.characteres.components.CharacterCardInfoData
import com.ifucolo.rickandmorty.ui.feature.characteres.viewmodel.CharacterViewModel
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = hiltViewModel(),
    characterId: Int,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(characterId) { viewModel.load(characterId) }

    when (val s = state) {
        is CharacterViewModel.UiState.Loading ->  {
            LoadingStateScreen()
        }
        is CharacterViewModel.UiState.Data -> {
            CharacterContent(
                characterDetail = s.character,
                onBackPressed = onBackPressed
            )
        }
        is CharacterViewModel.UiState.Error -> {
            ErrorStateScreen(
                onRetry = {
                    viewModel.load(characterId)
                },
                onBackPressed = onBackPressed
            )
        }
    }
}

const val CHARACTER_SCREEN_TEST_TAG = "character_screen_test_tag"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterContent(
    characterDetail: CharacterDetail,
    onBackPressed: () -> Unit
) {
    val snackBarController = rememberSnackbarController()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val successExportedText = stringResource(id = R.string.exported_success)
    val errorExportedText = stringResource(id = R.string.exported_success)

    val export = rememberCharacterJsonExportLauncher(characterDetail) { success, error ->
        if (success) snackBarController.show(message = successExportedText) else snackBarController.show(message = errorExportedText)
    }
    val actions = remember(characterDetail) {
        listOf(
            AppBarActions.Share { export() }
        )
    }

    val statusLabel = stringResource(id = R.string.label_status)
    val speciesLabel = stringResource(id = R.string.label_species)
    val originLabel = stringResource(id = R.string.label_origin)
    val episodeCountLabel = stringResource(id = R.string.label_episode_count)

    val characterInfoList = remember(characterDetail) {
        listOf(
            CharacterCardInfoData(
                icon = Icons.Default.Accessibility,
                label = statusLabel,
                value = characterDetail.status
            ),
            CharacterCardInfoData(
                icon = Icons.AutoMirrored.Default.HelpOutline,
                label = speciesLabel,
                value = characterDetail.species
            ),
            CharacterCardInfoData(
                icon = Icons.Default.Language,
                label = originLabel,
                value = characterDetail.originName
            ),
            CharacterCardInfoData(
                icon = Icons.AutoMirrored.Filled.List,
                label = episodeCountLabel,
                value = characterDetail.episodeCount.toString()
            )
        )
    }
    Scaffold(
        topBar = {
            AppTopBar(
                title = characterDetail.name,
                onBackClick = onBackPressed,
                scrollBehavior = scrollBehavior,
                actions = actions
            )
        },
        snackbarHost = { AppSnackbarHost(snackBarController) },
        modifier = Modifier
            .testTag(CHARACTER_SCREEN_TEST_TAG)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Dimensions.paddingMedium)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageComponent(
                imageRes = ImageResource.Url(characterDetail.imageUrl),
                title = characterDetail.name,
                modifier = Modifier
                    .size(Dimensions.iconSizeLarge)
                    .clip(CircleShape)
                    .border(
                        width = Dimensions.borderWidthMedium,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.height(Dimensions.paddingLarge))
            CharacterCardInfoComponent(
                data = characterInfoList
            )
        }
    }
}

@Preview(name = "CharacterScreen", showBackground = true, widthDp = 360)
@Composable
private fun CharacterContentPreviewLight() {
    RickAndMortyTheme {
        CharacterContent(
            characterDetail = sampleCharacterDetail(),
            onBackPressed = {}
        )
    }
}
@Composable
private fun sampleCharacterDetail() = CharacterDetail(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    species = "Human",
    originName = "Earth (C-137)",
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episodeCount = 51
)