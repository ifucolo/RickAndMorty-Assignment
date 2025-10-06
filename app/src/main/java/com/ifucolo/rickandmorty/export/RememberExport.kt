package com.ifucolo.rickandmorty.export


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ifucolo.rickandmorty.domain.CharacterDetail
@Composable
fun rememberCharacterJsonExportLauncher(
    character: CharacterDetail,
    onResult: (success: Boolean, error: Throwable?) -> Unit = { _, _ -> }
): () -> Unit {
    val context = LocalContext.current
    val suggestedName = remember(character) { buildSuggestedFileName(character) }

    val launcher = rememberLauncherForActivityResult(CreateDocument("text/plain")) { uri ->
        if (uri == null) {
            onResult(false, null)
        } else {
            runCatching {
                writeTextToUri(
                    context = context,
                    uri = uri,
                    text = characterToText(character),
                    onError = { onResult (false, null) }
                )
            }.onSuccess {
                onResult(true, null)
            }.onFailure { err ->
                onResult(false, err)
            }
        }
    }

    return remember(launcher, suggestedName) {
        { launcher.launch(suggestedName) }
    }
}