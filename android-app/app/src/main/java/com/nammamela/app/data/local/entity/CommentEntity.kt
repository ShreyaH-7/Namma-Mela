package com.nammamela.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val message: String,
    val time: String,
)
