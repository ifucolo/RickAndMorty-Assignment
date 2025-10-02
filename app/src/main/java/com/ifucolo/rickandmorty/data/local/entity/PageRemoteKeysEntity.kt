package com.ifucolo.rickandmorty.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_remote_keys")
data class PageRemoteKeysEntity(
    @PrimaryKey val label: String,
    val nextPage: Int?,
    val lastUpdated: Long
)