package com.confidential.data.database.dao

import androidx.room.*
import com.confidential.data.database.entity.ConnectionStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionStatusDao {

    @Query("SELECT * FROM connection_status ORDER BY arrival_time ASC LIMIT 1")
    fun getConnectionStatus(): Flow<ConnectionStatusEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ConnectionStatusEntity)

    @Update(entity = ConnectionStatusEntity::class)
    fun update(entity: ConnectionStatusEntity)

}