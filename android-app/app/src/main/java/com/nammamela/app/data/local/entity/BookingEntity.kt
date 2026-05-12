package com.nammamela.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val customerName: String,
    val seatsCsv: String,
    val timestamp: String,
)
