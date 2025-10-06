package com.ifucolo.rickandmorty.ui.feature.characteres.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifucolo.rickandmorty.ui.common.components.texts.BodyMediumText
import com.ifucolo.rickandmorty.ui.theme.Dimensions
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Immutable
data class CharacterCardInfoData(
    val icon: ImageVector,
    val label: String,
    val value: String
)
@Composable
fun CharacterCardInfoComponent(
    data: List<CharacterCardInfoData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.elevation)
    ) {
        Column {
            data.forEachIndexed { index, it ->
                DetailListItem(
                    icon = it.icon,
                    label = it.label,
                    value = it.value
                )
                if (index < data.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Dimensions.paddingMedium),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailListItem(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
        },
        headlineContent = { BodyMediumText(text = label, fontWeight = FontWeight.SemiBold) },
        supportingContent = { BodyMediumText(text = value) }
    )
}


@Preview(name = "CharacterCardInfo", showBackground = true)
@Composable
private fun CharacterCardInfoPreview() {
    RickAndMortyTheme {
        CharacterCardInfoComponent(
            data = sampleCharacterInfo()
        )
    }
}

@Composable
private fun sampleCharacterInfo(): List<CharacterCardInfoData> = listOf(
    CharacterCardInfoData(
        icon = Icons.Filled.Accessibility,
        label = "Status",
        value = "Alive"
    ),
    CharacterCardInfoData(
        icon = Icons.Filled.Info,
        label = "Species",
        value = "Human"
    ),
    CharacterCardInfoData(
        icon = Icons.Filled.Language,
        label = "Origin",
        value = "Earth (C-137)"
    )
)
