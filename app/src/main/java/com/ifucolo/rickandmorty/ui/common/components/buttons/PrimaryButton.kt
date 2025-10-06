package com.ifucolo.rickandmorty.ui.common.components.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ifucolo.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        leadingIcon?.let {
            it()
        }
        Text(text)
        trailingIcon?.let {
            it()
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    RickAndMortyTheme {
        PrimaryButton(
            onClick = {},
            text = "Primary Button"
        )
    }
}