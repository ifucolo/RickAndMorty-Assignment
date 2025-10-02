package com.ifucolo.rickandmorty.data.local.entity.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun listIntToString(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun stringToListInt(csv: String): List<Int> =
        if (csv.isBlank()) emptyList()
        else csv.split(",").mapNotNull { it.toIntOrNull() }
}