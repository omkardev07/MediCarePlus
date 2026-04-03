package com.example.medicareplus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val time: String,
    val isBeforeMeal: Boolean,
    val repeatDays: String,
    val status: String = "Pending",
    val hospitalName: String = "",
    val hospitalBranch: String = "",
    val hospitalNumber: String = "",
    val familyNumber: String = "",
    val retryCount: Int = 0,
    val maxRetries: Int = 3,
    val nextAlarmTime: Long = 0L,
    val isTaken: Boolean = false
)
