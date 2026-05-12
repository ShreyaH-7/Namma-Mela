package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammamela.app.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: BookingEntity)

    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun observeBookings(): Flow<List<BookingEntity>>

    @Query("DELETE FROM bookings")
    suspend fun clear()
}
