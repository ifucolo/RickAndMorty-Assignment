package com.ifucolo.rickandmorty.ui.feature.episode.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.ifucolo.rickandmorty.R
import com.ifucolo.rickandmorty.ui.common.components.list.CardListItem
import com.ifucolo.rickandmorty.ui.common.components.list.CardListItemData

const val CHARACTER_ITEM_TEST_TAG = "character_item_test_tag"
@Composable
fun CharacterItem(
    characterId: Int,
    onCharacterClick: (Int) -> Unit
) {
    val title = stringResource(R.string.characterId, characterId)
    val cardData = remember(characterId) {
        CardListItemData(
            title = title
        )
    }

    CardListItem(
        modifier = Modifier.testTag(CHARACTER_ITEM_TEST_TAG + "$characterId"),
        data = cardData,
        onCardClick = { onCharacterClick(characterId) }
    )
}
