package com.example.medicareplus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines ORDER BY nextAlarmTime ASC")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: Medicine): Long

    @Update
    suspend fun update(medicine: Medicine)

    @Delete
    suspend fun delete(medicine: Medicine)

    @Query("UPDATE medicines SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("UPDATE medicines SET isTaken = :isTaken, status = :status WHERE id = :id")
    suspend fun markTaken(id: Int, isTaken: Boolean, status: String)

    @Query("UPDATE medicines SET retryCount = :retryCount, nextAlarmTime = :nextAlarmTime WHERE id = :id")
    suspend fun updateRetryInfo(id: Int, retryCount: Int, nextAlarmTime: Long)

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Int): Medicine?

    // Notification History
    @Insert
    suspend fun insertHistory(history: NotificationHistory)

    @Query("SELECT * FROM notification_history ORDER BY time DESC")
    fun getHistory(): Flow<List<NotificationHistory>>
}
