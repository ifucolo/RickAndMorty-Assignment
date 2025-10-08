package com.ifucolo.rickandmorty.ui.feature.episode.episodes.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.domain.CharacterId
import com.ifucolo.rickandmorty.domain.Episode
import com.ifucolo.rickandmorty.ui.common.components.list.CardListItem
import com.ifucolo.rickandmorty.ui.common.components.list.CardListItemData
import com.ifucolo.rickandmorty.ui.common.components.list.EmptyItem
import com.ifucolo.rickandmorty.ui.common.components.list.EndOfTheListItem
import com.ifucolo.rickandmorty.ui.common.components.list.LastRefreshedItem
import com.ifucolo.rickandmorty.ui.common.components.list.ListLoadItem
import com.ifucolo.rickandmorty.ui.common.components.list.ListRetryItem
import com.ifucolo.rickandmorty.ui.common.components.snackbar.AppSnackbarHost
import com.ifucolo.rickandmorty.ui.common.components.snackbar.SnackbarController
import com.ifucolo.rickandmorty.ui.common.components.snackbar.rememberSnackbarController
import com.ifucolo.rickandmorty.ui.common.screens.ErrorStateScreen
import com.ifucolo.rickandmorty.ui.common.screens.LoadingStateScreen
import com.ifucolo.rickandmorty.ui.feature.episode.episodes.viewmodel.EpisodesViewModel
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun EpisodesScreen(
    viewModel: EpisodesViewModel = hiltViewModel(),
    onEpisodeClick: (Episode) -> Unit
) {
    val lazyPagingItems = viewModel.episodes.collectAsLazyPagingItems()
    val lastRefreshed = viewModel.lastRefreshed.collectAsStateWithLifecycle().value
    val snackBarController = rememberSnackbarController()
    val noInternetText = stringResource(R.string.no_internet_connection_message)
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(viewModel, lifecycle) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EpisodesViewModel.UiEvent.NoInternet -> {
                    snackBarController.show(message = noInternetText)
                }
            }
        }
    }

    val refreshLoadState = lazyPagingItems.loadState.refresh

    when {
        refreshLoadState is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
            ErrorStateScreen(onRetry = { lazyPagingItems.refresh() })
        }
        refreshLoadState is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
            LoadingStateScreen()
        }
        else -> {
            EpisodesContent(
                lazyPagingItems = lazyPagingItems,
                lastRefreshed = lastRefreshed,
                snackBarController = snackBarController,
                onPullToRefresh = { viewModel.onPullToRefresh { lazyPagingItems.refresh() } },
                onEpisodeClick = onEpisodeClick,
            )
        }
    }
}

@Composable
private fun EpisodesContent(
    lazyPagingItems: LazyPagingItems<Episode>,
    lastRefreshed: String,
    snackBarController: SnackbarController,
    onPullToRefresh: () -> Unit,
    onEpisodeClick: (Episode) -> Unit
) {
    val ptrState = rememberPullToRefreshState()
    val isRefreshing by remember(lazyPagingItems) {
        derivedStateOf { lazyPagingItems.loadState.refresh is LoadState.Loading }
    }
    Scaffold(
        modifier = Modifier.testTag("EpisodesScreen"),
        snackbarHost = { AppSnackbarHost(snackBarController) }
    ) { padding ->
        PullToRefreshBox(
            modifier = Modifier.padding(padding),
            state = ptrState,
            isRefreshing = isRefreshing,
            onRefresh = onPullToRefresh
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimensions.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium),
            ) {
                item(key = "last_refreshed") {
                    LastRefreshedItem(text = lastRefreshed)
                }

                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id }
                ) { index ->
                    lazyPagingItems[index]?.let { ep ->
                        EpisodeItem(episode = ep, onEpisodeClick = onEpisodeClick)
                    }
                }

                if (lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0) {
                    item { EmptyItem() }
                }

                when (val append = lazyPagingItems.loadState.append) {
                    is LoadState.Loading -> item { ListLoadItem() }
                    is LoadState.Error   -> item { ListRetryItem(onRetry = { lazyPagingItems.retry() }) }
                    is LoadState.NotLoading -> {
                        if (append.endOfPaginationReached && lazyPagingItems.itemCount > 0) {
                            item { EndOfTheListItem() }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun EpisodeItem(
    episode: Episode,
    onEpisodeClick: (Episode) -> Unit
) {
    val subTitle = listOf(
        stringResource(R.string.airDate, episode.airDateDisplay),
        stringResource(R.string.episodeCode, episode.code)
    )

    val cardData = remember(episode.id) {
        CardListItemData(title = episode.name, subTitle = subTitle)
    }

    CardListItem(
        data = cardData,
        onCardClick = { onEpisodeClick(episode) }
    )
}

@Preview(name = "Episode Item", showBackground = true, widthDp = 360)
@Composable
private fun EpisodeItemPreview() {
    RickAndMortyTheme {
        EpisodeItem(
            episode = sampleEpisode(),
            onEpisodeClick = {}
        )
    }
}

private fun sampleEpisode(
    id: Int = 42,
    name: String = "Rick Potion #9",
    air: String = "27/01/2014",
    code: String = "S01E06"
) = Episode(
    id = id,
    name = name,
    airDateDisplay = air,
    code = code,
    characterIds = listOf(CharacterId(1), CharacterId(2), CharacterId(3))
)