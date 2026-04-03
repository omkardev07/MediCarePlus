package com.example.medicareplus.data

import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val medicineDao: MedicineDao) {
    val allMedicines: Flow<List<Medicine>> = medicineDao.getAllMedicines()
    val notificationHistory: Flow<List<NotificationHistory>> = medicineDao.getHistory()

    suspend fun insert(medicine: Medicine): Long {
        return medicineDao.insert(medicine)
    }

    suspend fun update(medicine: Medicine) {
        medicineDao.update(medicine)
    }

    suspend fun delete(medicine: Medicine) {
        medicineDao.delete(medicine)
    }

    suspend fun getMedicineById(id: Int): Medicine? {
        return medicineDao.getMedicineById(id)
    }

    suspend fun updateStatus(id: Int, status: String) {
        medicineDao.updateStatus(id, status)
    }

    suspend fun markTaken(id: Int, isTaken: Boolean, status: String) {
        medicineDao.markTaken(id, isTaken, status)
    }

    suspend fun updateRetryInfo(id: Int, retryCount: Int, nextAlarmTime: Long) {
        medicineDao.updateRetryInfo(id, retryCount, nextAlarmTime)
    }

    suspend fun insertHistory(history: NotificationHistory) {
        medicineDao.insertHistory(history)
    }
}
