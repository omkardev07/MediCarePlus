package com.example.medicareplus

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.medicareplus.data.MedicineDatabase
import com.example.medicareplus.data.NotificationHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getIntExtra("MEDICINE_ID", -1)
        val medicineName = intent.getStringExtra("MEDICINE_NAME") ?: return
        val dosage = intent.getStringExtra("MEDICINE_DOSAGE") ?: ""
        val meal = intent.getStringExtra("MEDICINE_MEAL") ?: ""
        val hospName = intent.getStringExtra("HOSPITAL_NAME") ?: ""
        val hospNum = intent.getStringExtra("HOSPITAL_NUMBER") ?: ""
        val famNum = intent.getStringExtra("FAMILY_NUMBER") ?: ""

        // Cancel the notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(medicineId)

        // Cancel any pending auto-snooze alarm
        cancelAutoSnooze(context, medicineId)

        when (intent.action) {
            "ACTION_TAKEN" -> {
                Toast.makeText(context, "✅ Medicine Taken: $medicineName", Toast.LENGTH_SHORT).show()
                markAsTaken(context, medicineId)
                addHistory(context, medicineName, "Taken")
            }
            "ACTION_SNOOZE" -> {
                val count = intent.getIntExtra("SNOOZE_COUNT", 0)
                if (count < 3) {
                    val newCount = count + 1
                    snoozeAlarm(context, medicineId, medicineName, dosage, meal, hospName, hospNum, famNum, newCount)
                    Toast.makeText(context, "⏰ Snoozed for 1 min ($newCount/3)", Toast.LENGTH_SHORT).show()
                    addHistory(context, medicineName, "Snoozed ($newCount/3)")
                } else {
                    updateStatus(context, medicineId, "Missed")
                    addHistory(context, medicineName, "Missed after 3 snoozes")
                    Toast.makeText(context, "❌ Limit reached. Marked as missed.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cancelAutoSnooze(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.AUTO_SNOOZE_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, id + 5000, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    private fun markAsTaken(context: Context, id: Int) {
        if (id == -1) return
        val db = MedicineDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.medicineDao().markTaken(id, isTaken = true, status = "Taken")
        }
    }

    private fun updateStatus(context: Context, id: Int, status: String) {
        if (id == -1) return
        val db = MedicineDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.medicineDao().updateStatus(id, status)
        }
    }

    private fun addHistory(context: Context, name: String, action: String) {
        val db = MedicineDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.medicineDao().insertHistory(
                NotificationHistory(
                    medicineName = name,
                    time = System.currentTimeMillis(),
                    action = action
                )
            )
        }
    }

    private fun snoozeAlarm(
        context: Context, id: Int, name: String, dosage: String, meal: String,
        hospName: String, hospNum: String, famNum: String, newCount: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_ID", id)
            putExtra("MEDICINE_NAME", name)
            putExtra("MEDICINE_DOSAGE", dosage)
            putExtra("MEDICINE_MEAL", meal)
            putExtra("HOSPITAL_NAME", hospName)
            putExtra("HOSPITAL_NUMBER", hospNum)
            putExtra("FAMILY_NUMBER", famNum)
            putExtra("SNOOZE_COUNT", newCount)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Use setExactAndAllowWhileIdle for reliable 1 minute snooze
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 60_000L, // 1 minute snooze
            pendingIntent
        )

        // Update retry info in DB
        if (id != -1) {
            val db = MedicineDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                db.medicineDao().updateRetryInfo(
                    id, newCount, System.currentTimeMillis() + 60_000L
                )
            }
        }
    }
}
