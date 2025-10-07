package com.ifucolo.rickandmorty.ui.episodes

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.ifucolo.rickandmorty.ui.common.components.list.EMPTY_ITEM_TEST_TAG
import com.ifucolo.rickandmorty.ui.common.components.topbar.APP_BAR_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.episode.detail.CHARACTER_LIST_TEST_TAG
import com.ifucolo.rickandmorty.ui.feature.episode.detail.EpisodeDetailScreen
import com.ifucolo.rickandmorty.ui.feature.episode.detail.components.CHARACTER_ITEM_TEST_TAG
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class EpisodeDetailScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testEpisodesScreenWithCharacters() {
        rule.setContent {
            RickAndMortyTheme {
                EpisodeDetailScreen(
                    title = "Pilot",
                    items = (1..250).toList(),
                    onCharacterClick = {},
                    onBackPressed = {}
                )
            }
        }

        rule.onNodeWithTag(APP_BAR_TEST_TAG).assertIsDisplayed()
        rule.onNodeWithText("Pilot").assertIsDisplayed()
        rule.onNodeWithTag(CHARACTER_LIST_TEST_TAG).assertExists()
        rule.onNodeWithTag(EMPTY_ITEM_TEST_TAG).assertDoesNotExist()

        rule.onAllNodesWithTag(CHARACTER_ITEM_TEST_TAG).apply {
            fetchSemanticsNodes()
                .take(5)
                .forEachIndexed { index, _ -> {
                    val item = get(index)
                    item.assert(hasTestTag(CHARACTER_ITEM_TEST_TAG))
                    item.assertHasClickAction()
                }
            }
        }
        rule.onNodeWithTag(CHARACTER_LIST_TEST_TAG).performScrollToNode(
            hasTestTag(CHARACTER_ITEM_TEST_TAG + "250")
        )

        rule.waitForIdle()
    }

    @Test
    fun testEpisodesScreenWithNoCharacters() {
        rule.setContent {
            RickAndMortyTheme {
                EpisodeDetailScreen(
                    title = "Pilot",
                    items = emptyList(),
                    onCharacterClick = {},
                    onBackPressed = {}
                )
            }
        }

        rule.onNodeWithTag(APP_BAR_TEST_TAG).assertIsDisplayed()
        rule.onNodeWithText("Pilot").assertIsDisplayed()

        rule.onNodeWithTag(CHARACTER_LIST_TEST_TAG).assertExists()
        rule.onNodeWithText(CHARACTER_ITEM_TEST_TAG).assertDoesNotExist()
        rule.onNodeWithTag(EMPTY_ITEM_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testOnCharacterClick() {
        var clicked = false
        rule.setContent {
            EpisodeDetailScreen(
                title = "Pilot",
                items = (1..45).toList(),
                onCharacterClick = { clicked = true },
                onBackPressed = {}
            )
        }

        assertEquals(clicked, false)

        rule.onNodeWithTag(CHARACTER_ITEM_TEST_TAG + "1")
            .assertIsDisplayed()
            .performClick()

        rule.runOnIdle {
            assert(clicked)
        }

        assertEquals(clicked, true)
    }
}