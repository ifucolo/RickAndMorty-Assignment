package com.ifucolo.rickandmorty.data.mapper

import com.ifucolo.rickandmorty.data.local.mock.characterEntity
import com.ifucolo.rickandmorty.data.mock.characterDto
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterMappersTest {


    @Test
    fun characterDto_toEntity_all_fields() {
        val dto = characterDto()
        val entity = dto.toEntity()

        assertEquals(1, entity.id)
        assertEquals("Rick Sanchez", entity.name)
        assertEquals("Alive", entity.status)
        assertEquals("Human", entity.species)
        assertEquals("Earth (C-137)", entity.originName)
        assertEquals("https://example.com/rick.png", entity.imageUrl)
        assertEquals(3, entity.episodeCount) // size of dto.episode
    }

    @Test
    fun characterDto_toEntity_empty_episodes() {
        val dto = characterDto(
            id = 2,
            name = "Morty Smith",
            originName = "Earth (Replacement Dimension)",
            image = "https://example.com/morty.png",
            episodes = emptyList()
        )

        val entity = dto.toEntity()
        assertEquals(0, entity.episodeCount)
    }

    @Test
    fun characterEntity_toDomain_maps_all_fields() {
        val entity = characterEntity(
            id = 3,
            name = "Summer Smith"
        )

        val domain = entity.toDomain()

        assertEquals(3, domain.id)
        assertEquals("Summer Smith", domain.name)
        assertEquals("Alive", domain.status)
        assertEquals("Human", domain.species)
        assertEquals("Earth", domain.originName)
        assertEquals("https://example.com/summer.png", domain.imageUrl)
        assertEquals(51, domain.episodeCount)
    }
}