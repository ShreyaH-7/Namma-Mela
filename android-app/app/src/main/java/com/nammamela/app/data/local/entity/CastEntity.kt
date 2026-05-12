package com.nammamela.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cast")
data class CastEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String,
    val bio: String,
    val image: String,
)
