package com.example.medicareplus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_history")
data class NotificationHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineName: String,
    val time: Long,
    val action: String // "Taken", "Missed", "Snoozed"
)
