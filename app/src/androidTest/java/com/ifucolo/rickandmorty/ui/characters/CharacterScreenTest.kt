package com.ifucolo.rickandmorty.ui.characters

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ifucolo.rickandmorty.HiltComponentActivity
import com.ifucolo.rickandmorty.data.repository.mock.characterDetail
import com.ifucolo.rickandmorty.ui.common.components.image.IMAGE_COMPONENT_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.components.topbar.APP_BAR_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.screens.ERROR_STATE_SCREEN_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.screens.ERROR_TRY_AGAIN_BUTTON_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.screens.LOADING_SCREEN_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.characteres.components.CHARACTER_CARD_INFO_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.characteres.screen.CHARACTER_SCREEN_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.characteres.screen.CharacterScreen
import com.ifucolo.rickandmorty.ui.feature.characteres.viewmodel.CharacterViewModel
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CharacterScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltComponentActivity>()

    @Test
    fun testCharacterScreenLoadingErrorContent_withFakeViewModel_wholeFlow() = runTest {
        val fakeViewModel = FakeCharacterViewModel()
        fakeViewModel.setState(CharacterViewModel.UiState.Loading)

        rule.setContent {
            RickAndMortyTheme {
                CharacterScreen(
                    viewModel = fakeViewModel,
                    characterId = 1,
                    onBackPressed = {}
                )
            }
        }
        rule.onNodeWithTag(APP_BAR_TEST_TAG).assertIsNotDisplayed()

        //Loading State
        rule.onNodeWithTag(LOADING_SCREEN_TEST_TAG).assertIsDisplayed()
        fakeViewModel.setState(CharacterViewModel.UiState.Error(Throwable("Test Error")))
        rule.waitForIdle()
        //Error State
        rule.onNodeWithTag(APP_BAR_TEST_TAG).assertIsDisplayed()
        rule.onNodeWithTag(LOADING_SCREEN_TEST_TAG).assertIsNotDisplayed()
        rule.onNodeWithTag(ERROR_STATE_SCREEN_TEST_TAG).assertIsDisplayed()

        rule.onNodeWithTag(ERROR_TRY_AGAIN_BUTTON_TEST_TAG)
            .assertExists()
            .performClick()

        //Load Character Detail
        fakeViewModel.setState(CharacterViewModel.UiState.Data(characterDetail()))
        rule.waitForIdle()
        rule.onNodeWithTag(APP_BAR_TEST_TAG).assertIsDisplayed()
        rule.onNodeWithTag(ERROR_STATE_SCREEN_TEST_TAG).assertIsNotDisplayed()
        rule.onNodeWithTag(CHARACTER_SCREEN_TEST_TAG).assertIsDisplayed()

        rule.onNodeWithTag(IMAGE_COMPONENT_TEST_TAG).assertIsDisplayed()
        rule.onNodeWithContentDescription("Rick Sanchez").assertIsDisplayed()

        rule.onNodeWithTag(CHARACTER_CARD_INFO_TEST_TAG).assertIsDisplayed()

        rule.onNodeWithText("Alive").assertIsDisplayed()
        rule.onNodeWithText("Human").assertIsDisplayed()
        rule.onNodeWithText("Earth (C-137)").assertIsDisplayed()
        rule.onNodeWithText("51").assertIsDisplayed()
    }
}