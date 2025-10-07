package com.ifucolo.rickandmorty.ui.episodes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ifucolo.rickandmorty.HiltComponentActivity
import com.ifucolo.rickandmorty.data.repository.mock.episode
import com.ifucolo.rickandmorty.ui.common.screens.ERROR_STATE_SCREEN_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.screens.LOADING_SCREEN_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.episode.episodes.screen.EpisodesScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EpisodesScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var fakeViewModel: FakeEpisodesViewModel
    private lateinit var testScope: CoroutineScope

    @Before
    fun setup() {
        testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        fakeViewModel = FakeEpisodesViewModel(
            testScope = testScope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testEpisodesScreenLoading() {
        fakeViewModel = FakeEpisodesViewModel(
            testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )

        rule.setContent {
            EpisodesScreen(
                viewModel = fakeViewModel,
                onEpisodeClick = {}
            )
        }

        rule.onNodeWithTag(LOADING_SCREEN_TEST_TAG).assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testEpisodesScreenLoadingContent() {
        fakeViewModel = FakeEpisodesViewModel(
            testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
        val listEpisode = listOf(
            episode(),
            episode(id = 2, name = "Lawnmower Dog", code = "S01E02", airDateDisplay = "09/12/2013")
        )
        rule.setContent {
            EpisodesScreen(
                viewModel = fakeViewModel,
                onEpisodeClick = {}
            )
        }

        rule.onNodeWithTag(LOADING_SCREEN_TEST_TAG).assertIsDisplayed()

        rule.runOnIdle {
            fakeViewModel.fakeRepository.controllablePagingSource.completeLoad(
                data = listEpisode,
                prevKey = null,
                nextKey = null
            )
        }

        rule.onNodeWithText("Pilot").assertIsDisplayed()
        rule.onNodeWithText("Air date: 02/12/2013").assertIsDisplayed()
        rule.onNodeWithText("episode code: S01E01").assertIsDisplayed()

        rule.onNodeWithText("Lawnmower Dog").assertIsDisplayed()
        rule.onNodeWithText("Air date: 09/12/2013").assertIsDisplayed()
        rule.onNodeWithText("episode code: S01E02").assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testEpisodesScreenLoadingError() {
        fakeViewModel = FakeEpisodesViewModel(
            testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )

        rule.setContent {
            EpisodesScreen(
                viewModel = fakeViewModel,
                onEpisodeClick = {}
            )
        }

        rule.onNodeWithTag(LOADING_SCREEN_TEST_TAG).assertIsDisplayed()

        rule.runOnIdle {
            fakeViewModel.fakeRepository.controllablePagingSource.completeLoadWithError(
                Throwable("Test error")
            )
        }

        rule.onNodeWithTag(ERROR_STATE_SCREEN_TEST_TAG).assertIsDisplayed()
    }
}