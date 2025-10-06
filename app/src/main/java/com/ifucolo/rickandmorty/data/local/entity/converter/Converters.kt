package com.ifucolo.rickandmorty.data.local.entity.converter

import androidx.room.TypeConverter

class Converters {

    // This tells Room how to convert a List of Integers to a String
    @TypeConverter
    fun fromIntList(characterIds: List<Int>): String {
        return characterIds.joinToString(separator = ",")
    }

    // This tells Room how to convert a String back to a List of Integers
    @TypeConverter
    fun toIntList(characterIdsString: String): List<Int> {
        // Handle empty or blank strings to avoid errors
        if (characterIdsString.isBlank()) {
            return emptyList()
        }
        return characterIdsString.split(',').map { it.toInt() }
    }
}