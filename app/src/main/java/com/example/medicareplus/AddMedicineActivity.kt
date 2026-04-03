package com.example.medicareplus

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.medicareplus.data.Medicine
import com.example.medicareplus.data.MedicineDatabase
import com.example.medicareplus.databinding.ActivityAddMedicineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicineBinding
    private var selectedTime: String = ""
    private var selectedCalendar: Calendar = Calendar.getInstance()
    private var medicineId: Int = 0
    private var repeatDays: String = "Daily"

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        medicineId = intent.getIntExtra("MEDICINE_ID", 0)
        if (medicineId != 0) {
            supportActionBar?.title = "Edit Medicine"
            loadMedicineData()
        }

        // Request permissions
        requestNotificationPermission()
        checkExactAlarmPermission()

        // Default repeat selection
        binding.toggleRepeat.check(R.id.btnDaily)

        binding.btnTimePicker.setOnClickListener {
            showDateTimePicker()
        }

        // Listen for repeat selection
        binding.toggleRepeat.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                repeatDays = when (checkedId) {
                    R.id.btnDaily -> "Daily"
                    R.id.btnWeekly -> "Weekly"
                    R.id.btnCustom -> "Once"
                    else -> "Daily"
                }
            }
        }

        binding.btnSaveMedicine.setOnClickListener {
            saveMedicine()
        }
    }

    private fun loadMedicineData() {
        lifecycleScope.launch {
            val medicine = withContext(Dispatchers.IO) {
                MedicineDatabase.getDatabase(this@AddMedicineActivity).medicineDao().getMedicineById(medicineId)
            }
            medicine?.let {
                binding.etMedicineName.setText(it.name)
                binding.etDosage.setText(it.dosage)
                selectedTime = it.time
                binding.btnTimePicker.text = "📅 $selectedTime"
                
                if (it.isBeforeMeal) {
                    binding.toggleMeal.check(R.id.btnBeforeMeal)
                } else {
                    binding.toggleMeal.check(R.id.btnAfterMeal)
                }

                when (it.repeatDays) {
                    "Daily" -> binding.toggleRepeat.check(R.id.btnDaily)
                    "Weekly" -> binding.toggleRepeat.check(R.id.btnWeekly)
                    "Once" -> binding.toggleRepeat.check(R.id.btnCustom)
                }
                
                selectedCalendar.timeInMillis = it.nextAlarmTime
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            selectedCalendar.set(Calendar.YEAR, year)
            selectedCalendar.set(Calendar.MONTH, month)
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedCalendar.set(Calendar.MINUTE, minute)
                selectedCalendar.set(Calendar.SECOND, 0)
                selectedCalendar.set(Calendar.MILLISECOND, 0)

                selectedTime = String.format(
                    Locale.getDefault(), "%02d/%02d/%04d %02d:%02d %s",
                    dayOfMonth, month + 1, year,
                    if (hourOfDay % 12 == 0) 12 else hourOfDay % 12,
                    minute,
                    if (hourOfDay < 12) "AM" else "PM"
                )
                binding.btnTimePicker.text = "📅 $selectedTime"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            timePicker.show()

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun saveMedicine() {
        val name = binding.etMedicineName.text.toString().trim()
        val dosage = binding.etDosage.text.toString().trim()
        val isBeforeMeal = binding.toggleMeal.checkedButtonId == R.id.btnBeforeMeal

        if (name.isEmpty() || dosage.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please fill all required details", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCalendar.timeInMillis <= System.currentTimeMillis()) {
            Toast.makeText(this, "Please select a future date and time", Toast.LENGTH_SHORT).show()
            return
        }

        // Get stored hospital and family details
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = prefs.getString("logged_in_user", "User") ?: "User"
        val hospName = prefs.getString("${username}_hosp_name", "") ?: ""
        val hospNum = prefs.getString("${username}_hosp_number", "") ?: ""
        val famNum = prefs.getString("${username}_fam_number", "") ?: ""

        val medicine = Medicine(
            id = medicineId,
            name = name,
            dosage = dosage,
            time = selectedTime,
            isBeforeMeal = isBeforeMeal,
            repeatDays = repeatDays,
            hospitalName = hospName,
            hospitalNumber = hospNum,
            familyNumber = famNum,
            nextAlarmTime = selectedCalendar.timeInMillis
        )

        lifecycleScope.launch {
            val dao = MedicineDatabase.getDatabase(this@AddMedicineActivity).medicineDao()
            val actualId: Int

            if (medicineId == 0) {
                val rowId = withContext(Dispatchers.IO) { dao.insert(medicine) }
                actualId = rowId.toInt()
            } else {
                withContext(Dispatchers.IO) { dao.update(medicine) }
                actualId = medicineId
            }

            scheduleAlarm(medicine.copy(id = actualId))
            Toast.makeText(this@AddMedicineActivity, "✅ Medicine Saved & Alarm Set!", Toast.LENGTH_SHORT).show()
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun scheduleAlarm(medicine: Medicine) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val meal = if (medicine.isBeforeMeal) "Before Meal" else "After Meal"

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_ID", medicine.id)
            putExtra("MEDICINE_NAME", medicine.name)
            putExtra("MEDICINE_DOSAGE", medicine.dosage)
            putExtra("MEDICINE_MEAL", meal)
            putExtra("HOSPITAL_NAME", medicine.hospitalName)
            putExtra("HOSPITAL_NUMBER", medicine.hospitalNumber)
            putExtra("FAMILY_NUMBER", medicine.familyNumber)
            putExtra("SNOOZE_COUNT", 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, medicine.id, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            medicine.nextAlarmTime,
            pendingIntent
        )
    }
}
