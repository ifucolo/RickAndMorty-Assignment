package com.ifucolo.rickandmorty.util

private val monthIndex = mapOf(
    "January" to 1, "February" to 2, "March" to 3, "April" to 4, "May" to 5, "June" to 6,
    "July" to 7, "August" to 8, "September" to 9, "October" to 10, "November" to 11, "December" to 12
)

/** Converts Rick & Morty API air_date strings to dd/MM/yyyy. Falls back to the original if parsing fails. */
fun rnmAirDateToDdMMyyyy(src: String): String = runCatching {
    val parts = src.replace(",", "").split(" ")
    val m = monthIndex[parts[0]]!!.toString().padStart(2, '0')
    val d = parts[1].padStart(2, '0')
    val y = parts[2]
    "$d/$m/$y"
}.getOrElse { src }

/** Extract integer IDs from character URLs like ".../character/1". */
fun characterIdsFromUrls(urls: List<String>): List<Int> =
    urls.mapNotNull { it.substringAfterLast("/").toIntOrNull() }