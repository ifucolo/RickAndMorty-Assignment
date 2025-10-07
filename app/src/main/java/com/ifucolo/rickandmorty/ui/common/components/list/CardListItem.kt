package com.ifucolo.rickandmorty.ui.common.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyLargeText
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText
import com.ifucolo.rickandmorty.ui.theme.Dimensions

data class CardListItemData(
    val title: String,
    val subTitle: List<String> = emptyList()
)

@Composable
fun CardListItem(
    modifier: Modifier = Modifier,
    data: CardListItemData,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
            ) {
                BodyLargeText(
                    text = data.title,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if(data.subTitle.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        data.subTitle.forEachIndexed { index, subtitle ->
                            BodyMediumText(
                                text = subtitle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Go to details",
                modifier = Modifier.size(Dimensions.paddingMedium),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}