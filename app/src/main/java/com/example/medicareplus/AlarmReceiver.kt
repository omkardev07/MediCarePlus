package com.example.medicareplus

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.medicareplus.data.MedicineDatabase
import com.example.medicareplus.data.NotificationHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "medicine_alarm_channel"
        const val AUTO_SNOOZE_ACTION = "com.example.medicareplus.AUTO_SNOOZE"
        const val AUTO_SNOOZE_DELAY_MS = 60_000L // 1 minute wait before auto-snooze
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Acquire a partial wake lock so the device stays awake while we show the notification
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "MediCarePlus::AlarmWakeLock"
        )
        wakeLock.acquire(10_000L) // 10 seconds max

        try {
            val id = intent.getIntExtra("MEDICINE_ID", -1)
            val name = intent.getStringExtra("MEDICINE_NAME") ?: "Medicine"
            val dosage = intent.getStringExtra("MEDICINE_DOSAGE") ?: ""
            val meal = intent.getStringExtra("MEDICINE_MEAL") ?: ""
            val hospName = intent.getStringExtra("HOSPITAL_NAME") ?: ""
            val hospNum = intent.getStringExtra("HOSPITAL_NUMBER") ?: ""
            val famNum = intent.getStringExtra("FAMILY_NUMBER") ?: ""
            val count = intent.getIntExtra("SNOOZE_COUNT", 0)

            val isAutoSnooze = intent.action == AUTO_SNOOZE_ACTION

            if (isAutoSnooze) {
                // This is an auto-snooze trigger (user didn't respond)
                handleAutoSnooze(context, id, name, dosage, meal, hospName, hospNum, famNum, count)
            } else {
                // Normal alarm: show notification and schedule auto-snooze fallback
                showNotification(context, id, name, dosage, meal, hospName, hospNum, famNum, count)
                scheduleAutoSnooze(context, id, name, dosage, meal, hospName, hospNum, famNum, count)
            }
        } finally {
            if (wakeLock.isHeld) {
                wakeLock.release()
            }
        }
    }

    private fun handleAutoSnooze(
        context: Context, id: Int, name: String, dosage: String, meal: String,
        hospName: String, hospNum: String, famNum: String, count: Int
    ) {
        if (count < 3) {
            // Auto-snooze: reschedule alarm for 1 minute later with incremented count
            val newCount = count + 1
            addHistory(context, name, "Auto-snoozed ($newCount/3) - No response")

            // Cancel existing notification
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(id)

            // Schedule next alarm in 1 minute
            scheduleSnoozeAlarm(context, id, name, dosage, meal, hospName, hospNum, famNum, newCount)
        } else {
            // Max retries reached, mark as MISSED
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(id)

            updateStatus(context, id, "Missed")
            addHistory(context, name, "Missed - No response after 3 retries")

            // Show final missed notification
            showMissedNotification(context, id, name, dosage)
        }
    }

    private fun showNotification(
        context: Context, id: Int, name: String, dosage: String, meal: String,
        hospName: String, hospNum: String, famNum: String, count: Int
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create high-priority notification channel with sound and vibration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medicine Alarm Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent medicine reminder alarms"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
                setSound(
                    alarmSound,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setBypassDnd(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        // Fetch User Name
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val usernameRaw = prefs.getString("logged_in_user", "User") ?: "User"
        val fullName = prefs.getString("${usernameRaw}_fullname", usernameRaw) ?: usernameRaw
        val firstName = fullName.split(" ").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "User"

        // Motivational Medical Message
        val messages = listOf(
            "Your health is your greatest wealth. Please take your medicine on time!",
            "Consistency is the key to recovery. Keep your health on track!",
            "Taking your medicine right now keeps you strong and healthy.",
            "A gentle reminder: self-care starts with taking your prescribed dose."
        )
        val motivation = messages.random()

        val details = buildString {
            append("Hi $firstName! 👋\n\n")
            append("It's time to take your prescribed dose:\n")
            append("💊 $name ($dosage) • $meal\n")
            
            if (hospName.isNotEmpty()) append("\n🏥 Hospital: $hospName")
            if (famNum.isNotEmpty()) append("\n👨‍👩‍👧 Family: $famNum")
            
            append("\n\n🌟 $motivation")
            if (count > 0) append("\n\n⚠️ Reminder $count/3 - We care about your health, please take it!")
        }

        // YES action - Mark as taken
        val yesIntent = Intent(context, ActionReceiver::class.java).apply {
            action = "ACTION_TAKEN"
            putExtra("MEDICINE_ID", id)
            putExtra("MEDICINE_NAME", name)
            putExtra("SNOOZE_COUNT", count)
        }
        val yesPending = PendingIntent.getBroadcast(
            context, id * 10 + 1, yesIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // NO action - Snooze (will automatically snooze 1 min in backend)
        val noIntent = Intent(context, ActionReceiver::class.java).apply {
            action = "ACTION_SNOOZE"
            putExtra("MEDICINE_ID", id)
            putExtra("MEDICINE_NAME", name)
            putExtra("MEDICINE_DOSAGE", dosage)
            putExtra("MEDICINE_MEAL", meal)
            putExtra("HOSPITAL_NAME", hospName)
            putExtra("HOSPITAL_NUMBER", hospNum)
            putExtra("FAMILY_NUMBER", famNum)
            putExtra("SNOOZE_COUNT", count)
        }
        val noPending = PendingIntent.getBroadcast(
            context, id * 10 + 2, noIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Full-screen intent to wake screen
        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPending = PendingIntent.getActivity(
            context, id * 10 + 3, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Make the notification look premium with color and large icon
        val largeIcon = android.graphics.BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setLargeIcon(largeIcon)
            .setColor(0xFF007AFF.toInt()) // Premium electric blue
            .setContentTitle("⏰ Medicine Time, $firstName!")
            .setContentText("Your $name ($dosage) is due now.")
            .setStyle(NotificationCompat.BigTextStyle().bigText(details))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(alarmSound)
            .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(fullScreenPending, true)
            // Clean option buttons ensuring 1 min text is gone
            .addAction(android.R.drawable.ic_input_add, "✅ YES - Taken", yesPending)
            .addAction(android.R.drawable.ic_delete, "❌ NO - Snooze", noPending)

        val notification = builder.build()
        notification.flags = notification.flags or NotificationCompat.FLAG_INSISTENT
        manager.notify(id, notification)
    }

    private fun showMissedNotification(context: Context, id: Int, name: String, dosage: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("❌ Missed Medicine: $name")
            .setContentText("You missed $dosage. No response after 3 reminders.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)

        // Use a different notification ID for the missed notification
        manager.notify(id + 10000, builder.build())
    }

    private fun scheduleAutoSnooze(
        context: Context, id: Int, name: String, dosage: String, meal: String,
        hospName: String, hospNum: String, famNum: String, count: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AUTO_SNOOZE_ACTION
            putExtra("MEDICINE_ID", id)
            putExtra("MEDICINE_NAME", name)
            putExtra("MEDICINE_DOSAGE", dosage)
            putExtra("MEDICINE_MEAL", meal)
            putExtra("HOSPITAL_NAME", hospName)
            putExtra("HOSPITAL_NUMBER", hospNum)
            putExtra("FAMILY_NUMBER", famNum)
            putExtra("SNOOZE_COUNT", count)
        }

        // Use a unique request code for auto-snooze (offset by 5000)
        val pendingIntent = PendingIntent.getBroadcast(
            context, id + 5000, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + AUTO_SNOOZE_DELAY_MS,
            pendingIntent
        )
    }

    private fun scheduleSnoozeAlarm(
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

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 60_000L, // 1 minute
            pendingIntent
        )
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
                NotificationHistory(medicineName = name, time = System.currentTimeMillis(), action = action)
            )
        }
    }
}
