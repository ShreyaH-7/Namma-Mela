package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammamela.app.data.local.entity.CastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(cast: List<CastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(castEntity: CastEntity)

    @Query("SELECT * FROM cast ORDER BY name ASC")
    fun observeCast(): Flow<List<CastEntity>>

    @Query("DELETE FROM cast")
    suspend fun clear()
}
