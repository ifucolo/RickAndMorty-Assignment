package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Preview(showBackground = true)
@Composable
fun ListComponentsPreview() {
    RickAndMortyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            CardListItem(

                data = CardListItemData(
                    title = "Character Name",
                    subTitle = listOf("Status", "Species", "Origin")
                ),
                onCardClick = {}
            )
            EmptyItem()
            EndOfTheListItem()
            LastRefreshedItem("Just now")
            ListLoadItem()
            ListRetryItem {  }
        }
    }
}