package com.ifucolo.rickandmorty.ui.common.components.texts

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme
import com.ifucolo.rickandmorty.ui.theme.AppTypography


@Composable
fun TitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = AppTypography.titleLarge,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    GenericText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun BodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = AppTypography.bodyLarge,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    GenericText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines
    )
}


@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = AppTypography.bodyMedium,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE
) {
    GenericText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        maxLines = maxLines
    )
}


@Preview(showBackground = true)
@Composable
fun TextPreviews() {
    RickAndMortyTheme {
        Column {
            TitleLargeText(text = "Title Large")
            BodyLargeText(text = "Body Large")
            BodyMediumText(text = "Body Medium")
        }
    }
}