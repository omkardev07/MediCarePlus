package com.example.medicareplus

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medicareplus.data.MedicineDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Re-schedules all pending medicine alarms after device reboot.
 * Alarms set via AlarmManager are lost when the device reboots,
 * so this receiver reschedules them from the database.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            rescheduleAlarms(context)
        }
    }

    private fun rescheduleAlarms(context: Context) {
        val db = MedicineDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val medicines = db.medicineDao().getAllMedicines().first()
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            for (medicine in medicines) {
                // Only reschedule medicines that are still pending and have a future alarm time
                if (medicine.status == "Pending" && !medicine.isTaken && medicine.nextAlarmTime > System.currentTimeMillis()) {
                    val meal = if (medicine.isBeforeMeal) "Before Meal" else "After Meal"

                    val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                        putExtra("MEDICINE_ID", medicine.id)
                        putExtra("MEDICINE_NAME", medicine.name)
                        putExtra("MEDICINE_DOSAGE", medicine.dosage)
                        putExtra("MEDICINE_MEAL", meal)
                        putExtra("HOSPITAL_NAME", medicine.hospitalName)
                        putExtra("HOSPITAL_NUMBER", medicine.hospitalNumber)
                        putExtra("FAMILY_NUMBER", medicine.familyNumber)
                        putExtra("SNOOZE_COUNT", medicine.retryCount)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        context, medicine.id, alarmIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        medicine.nextAlarmTime,
                        pendingIntent
                    )
                }
            }
        }
    }
}
