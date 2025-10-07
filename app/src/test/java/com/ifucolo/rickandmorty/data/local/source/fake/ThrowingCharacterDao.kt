package com.ifucolo.rickandmorty.data.local.source.fake

import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity

class ThrowingCharacterDao : CharacterDao {
        override suspend fun getById(id: Int): CharacterEntity? {
            throw RuntimeException("boom-get")
        }
        override suspend fun upsert(entity: CharacterEntity) {
            throw RuntimeException("boom-upsert")
        }
        override suspend fun upsertAll(items: List<CharacterEntity>) {
            throw RuntimeException("boom-upsertAll")
        }
    }