package com.example.medicareplus.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.medicareplus.data.Medicine
import com.example.medicareplus.data.MedicineDatabase
import com.example.medicareplus.data.MedicineRepository
import com.example.medicareplus.data.NotificationHistory
import kotlinx.coroutines.launch

class MedicineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MedicineRepository
    val allMedicines: LiveData<List<Medicine>>
    val notificationHistory: LiveData<List<NotificationHistory>>

    init {
        val medicineDao = MedicineDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(medicineDao)
        allMedicines = repository.allMedicines.asLiveData()
        notificationHistory = repository.notificationHistory.asLiveData()
    }

    fun insert(medicine: Medicine) = viewModelScope.launch {
        repository.insert(medicine)
    }

    fun update(medicine: Medicine) = viewModelScope.launch {
        repository.update(medicine)
    }

    fun delete(medicine: Medicine) = viewModelScope.launch {
        repository.delete(medicine)
    }

    fun insertHistory(history: NotificationHistory) = viewModelScope.launch {
        repository.insertHistory(history)
    }
}
