package com.confidential.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.confidential.data.database.dao.ConnectionStatusDao
import com.confidential.data.database.entity.ConnectionStatusEntity

@Database(entities = [ConnectionStatusEntity::class], version = 1)
abstract class ConfidentialDatabase : RoomDatabase() {

    abstract fun connectionStatusDao(): ConnectionStatusDao


}