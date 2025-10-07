package com.ifucolo.rickandmorty.ui.feature.episode.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.ifucolo.rickandmorty.ui.common.components.list.EmptyItem
import com.ifucolo.rickandmorty.ui.common.components.topbar.AppTopBar
import com.ifucolo.rickandmorty.ui.feature.episode.detail.components.CharacterItem
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailScreen(
    title: String,
    items: List<Int>,
    onCharacterClick: (Int) -> Unit,
    onBackPressed: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                onBackClick = onBackPressed,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CharacterList(
            items = items,
            onCharacterClick = onCharacterClick,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

const val CHARACTER_LIST_TEST_TAG = "character_list_test_tag"
@Composable
private fun CharacterList(
    items: List<Int>,
    modifier: Modifier = Modifier,
    onCharacterClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.testTag(CHARACTER_LIST_TEST_TAG).padding(horizontal = Dimensions.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium),
        contentPadding = PaddingValues(vertical = Dimensions.paddingMedium)
    ) {
        if (items.isEmpty()) {
            item { EmptyItem() }
        } else {
            items(
                items = items,
                key = { it }
            ) { id ->
                CharacterItem(
                    characterId = id,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}

@Preview(name = "Episode Detail – With Characters", showBackground = true, widthDp = 360)
@Composable
private fun EpisodeDetailScreenPreview_Populated() {
    RickAndMortyTheme {
        EpisodeDetailScreen(
            title = "S01E01 • Pilot",
            items = listOf(1, 2, 35, 183),
            onCharacterClick = {},
            onBackPressed = {}
        )
    }
}

@Preview(name = "Episode Detail – Empty", showBackground = true, widthDp = 360)
@Composable
private fun EpisodeDetailScreenPreview_Empty() {
    RickAndMortyTheme {
        EpisodeDetailScreen(
            title = "S01E01 • Pilot",
            items = emptyList(),
            onCharacterClick = {},
            onBackPressed = {}
        )
    }
}