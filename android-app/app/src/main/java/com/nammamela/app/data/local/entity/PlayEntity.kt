package com.nammamela.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play")
data class PlayEntity(
    @PrimaryKey val id: String,
    val title: String,
    val genre: String,
    val duration: String,
    val description: String,
    val poster: String,
)
