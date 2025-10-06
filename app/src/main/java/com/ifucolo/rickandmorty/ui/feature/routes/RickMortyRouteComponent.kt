package com.ifucolo.rickandmorty.ui.feature.routes

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ifucolo.rickandmorty.ui.feature.characteres.screen.CharacterScreen
import com.ifucolo.rickandmorty.ui.feature.episode.detail.EpisodeDetailScreen
import com.ifucolo.rickandmorty.ui.feature.episode.episodes.screen.EpisodesScreen
import com.ifucolo.rickandmorty.ui.feature.routes.viewmodel.RickMortyRouteViewModel

@Composable
fun RickMortyRouteComponent(
    viewModel: RickMortyRouteViewModel = hiltViewModel()
) {
    val backStack = viewModel.backStack
    val context = LocalContext.current
    BackHandler(enabled = backStack.size > 1) {
        viewModel.popBackStack()
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            if (!viewModel.popBackStack()) {
                (context as? Activity)?.finish()
            }
        },
        entryProvider = entryProvider {
            entry<RickMortyRoutes.EpisodeList> {
                EpisodesScreen(
                    onEpisodeClick = { episode ->
                        viewModel.navigateTo(
                            route = RickMortyRoutes.EpisodeDetail(title = episode.name, charactersIds = episode.characterIds)
                        )
                    }
                )
            }
            entry<RickMortyRoutes.EpisodeDetail> {
                EpisodeDetailScreen(
                    items = it.charactersIds,
                    title = it.title,
                    onBackPressed = { viewModel.popBackStack() },
                    onCharacterClick = { characterId ->
                        viewModel.navigateTo(route = RickMortyRoutes.CharacterDetail(characterId =  characterId))
                    }
                )
            }
            entry<RickMortyRoutes.CharacterDetail> {
                CharacterScreen(
                    characterId = it.characterId,
                    onBackPressed = { viewModel.popBackStack() }
                )
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}