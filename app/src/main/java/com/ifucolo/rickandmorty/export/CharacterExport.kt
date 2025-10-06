package com.ifucolo.rickandmorty.export

import android.content.Context
import android.net.Uri
import com.ifucolo.rickandmorty.domain.CharacterDetail
import java.io.OutputStreamWriter

fun buildSuggestedFileName(character: CharacterDetail): String {
    val safe = character.name.replace(Regex("""[^\w\-]+"""), "_")
    return "${safe}_character.txt"
}

fun characterToText(c: CharacterDetail): String = buildString {
    appendLine("Character")
    appendLine("=========")
    appendLine("Name          : ${c.name}")
    appendLine("Status        : ${c.status}")
    appendLine("Species       : ${c.species}")
    appendLine("Origin        : ${c.originName}")
    appendLine("Episode count : ${c.episodeCount}")
}

fun writeTextToUri(context: Context, uri: Uri, text: String, onError: () -> Unit = {}){
    context.contentResolver.openOutputStream(uri)?.use { os ->
        OutputStreamWriter(os).use { it.write(text) }
    } ?: { onError() }
}