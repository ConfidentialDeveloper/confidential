package com.confidential.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection_status")
data class ConnectionStatusEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("is_public_sent") val isSent: Boolean,
    @ColumnInfo("is_public_received") val isReceived: Boolean,
    @ColumnInfo("departure_time") val departureTime: Long,
    @ColumnInfo("arrival_time") val arrivalTime: Long
)
