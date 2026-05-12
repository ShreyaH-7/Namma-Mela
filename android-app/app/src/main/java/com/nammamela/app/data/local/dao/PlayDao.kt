package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammamela.app.data.local.entity.PlayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(play: PlayEntity)

    @Query("SELECT * FROM play LIMIT 1")
    fun observePlay(): Flow<PlayEntity?>
}
