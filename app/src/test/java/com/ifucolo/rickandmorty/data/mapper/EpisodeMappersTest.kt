package com.ifucolo.rickandmorty.data.mapper

import com.ifucolo.rickandmorty.data.local.mock.episodeEntity
import com.ifucolo.rickandmorty.data.mock.episodeDto
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EpisodeMappersTest {

    @Test
    fun episodeDto_toEntity_maps_all_fields() {
        val dto = episodeDto()

        val entity = dto.toEntity()

        assertEquals(6, entity.id)
        assertEquals("Rick Potion #9", entity.name)

        assertEquals("27/01/2014", entity.airDate)
        assertNotEquals("January 1, 2013", entity.airDate)

        assertEquals("S01E06", entity.code)
        assertEquals(listOf(1, 2, 28), entity.characterIds)
    }

    @Test
    fun episodeDto_toEntity_empty_characters() {
        val dto = episodeDto(
            id = 1,
            name = "Pilot",
            airDate = "December 2, 2013",
            episode = "S01E01",
            characters = emptyList()
        )

        val entity = dto.toEntity()

        assertTrue(entity.characterIds.isEmpty())
        assertEquals("02/12/2013", entity.airDate) // date still converted
    }

    @Test
    fun episodeDto_toEntity_ignores_malformed_character_urls() {
        val dto = episodeDto(
            id = 2,
            name = "Lawnmower Dog",
            airDate = "December 9, 2013",
            episode = "S01E02",
            characters = listOf(
                "https://rickandmortyapi.com/api/character/1",
                "bad://url",
                "https://rickandmortyapi.com/api/character/not-a-number",
                "https://rickandmortyapi.com/api/character/8"
            )
        )

        val entity = dto.toEntity()

        assertEquals(listOf(1, 8), entity.characterIds)
        assertEquals("09/12/2013", entity.airDate)
    }

    @Test
    fun episodeEntity_toDomain_maps_all_fields() {
        val entity = episodeEntity(
            id = 10,
            name = "Close Rick-counters of the Rick Kind"
        )

        val domain = entity.toDomain()

        assertEquals(10, domain.id)
        assertEquals("Close Rick-counters of the Rick Kind", domain.name)
        assertEquals("December 2, 2013", domain.airDateDisplay)
        assertEquals("S01E10", domain.code)
        assertEquals(listOf(1, 2), domain.characterIds)
    }
}