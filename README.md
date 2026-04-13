<p align="center">
  <img src="assets/banner.png" alt="MediCare+ Banner" width="600"/>
</p>

<h1 align="center">💊 MediCare+</h1>

<p align="center">
  <b>Your health, simplified.</b><br>
  A smart medicine reminder app built with modern Android architecture.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Database-Room-007AFF?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF5722?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge"/>
</p>

---

## 🎯 Problem Statement

Medication non-adherence is a major global health challenge, leading to worsened health outcomes, preventable hospitalizations, and increased healthcare costs. Patients often forget to take their medications on time due to busy schedules, complex prescriptions, or simply losing track of time. Relying on memory or basic alarm clocks is often insufficient, especially for elderly patients or individuals managing chronic conditions with multiple medications.

## 💡 The Solution: MediCare+

**MediCare+** addresses this issue by providing an intelligent, persistent, and highly customizable medication reminder system. It goes beyond simple alarms by utilizing a robust **Auto-Snooze & Retry Mechanism** that guarantees users are reminded multiple times until they explicitly confirm they have taken their medicine. 

The application is designed with a **premium, accessible UI** to ensure ease of use for all age groups, coupling medication schedules with hospital information and emergency family contacts into one centralized health hub.

---

## ✨ Key Features

### 💊 Intelligent Medicine Management
- **Add, Edit & Delete** medicines with precise dosages, time schedules, and "Before/After Meal" indicators.
- **Flexible Repeat Options** (Daily, Weekly, One-time) with strict future-time validation.

### ⏰ Resilient Alarm System (Core Technical Feature)
- Utilizes `AlarmManager.setExactAndAllowWhileIdle()` to reliably trigger alarms even when the device is in Doze mode.
- **3-Retry Auto-Snooze:** If a notification is dismissed or ignored, the app schedules a fallback alarm one minute later, repeating up to three times before logging a "Missed" dose.
- **Boot Persistence:** A `BootReceiver` restores all pending alarms automatically upon device restart.
- **Wake Lock Support:** Utilizes `PowerManager.PARTIAL_WAKE_LOCK` and full-screen intents to wake the device screen for urgent reminders.

### 🔔 Interactive Notifications
- High-priority `NotificationChannel` with custom sound, vibration patterns, and lock-screen visibility.
- Actionable buttons (✅ **Taken**, ❌ **Snooze**) integrated directly into the notification using `PendingIntent` and `BroadcastReceiver`.

### 🏥 & 👨‍👩‍👧 Emergency Context
- **Hospital Connect:** Locally caches hospital details (Name, Branch, Doctor, Contact) linked to the user's profile.
- **Family Connect:** Stores primary and secondary emergency family contacts. Both hospital and family details are surfaced within medication reminders.

### 📋 Full Logging & History
- Complete, timestamped, and color-coded history log tracking exactly when medications were **Taken**, **Missed**, or **Snoozed**.

---

## 🏗️ Architecture & Technical Details

MediCare+ is built using modern Android development practices, emphasizing separation of concerns, scalability, and robust background processing.

| Component | Implementation Details |
|-----------|------------------------|
| **Architecture** | **MVVM** (Model-View-ViewModel) isolating UI logic from data layers. |
| **Local Database** | **Room Persistence Library** with native SQLite. Uses `KSP` for annotation processing without exporting schemas for lean builds. |
| **Concurrency** | **Kotlin Coroutines** and **Flow** for asynchronous database operations and reactive UI updates. |
| **Background Work** | `AlarmManager` for precise scheduling, combined with explicit `BroadcastReceiver`s (`AlarmReceiver`, `ActionReceiver`, `BootReceiver`) to handle background triggers without running constant background services. |
| **UI Components** | Traditional XML Layouts leveraging **ViewBinding** for null-safe view references and **Material Design 3** cards/buttons. |
| **Dependencies** | Target SDK 35, Gradle 8.10.2, AGP 8.7.3. |

### Data Flow Example (Alarm Trigger)
1. `AlarmManager` triggers `AlarmReceiver`.
2. `AlarmReceiver` acquires a 10s `WakeLock` and builds a high-priority Notification.
3. Automatically schedules a fallback auto-snooze alarm in the SQLite DB via Coroutines.
4. User clicks "Taken" ➡️ `ActionReceiver` captures intent ➡️ Coroutine updates DB status to "Taken" ➡️ Cancels auto-snooze alarm.

---

## 📁 Project Structure

```text
app/src/main/java/com/example/medicareplus/
├── data/
│   ├── Medicine.kt              # Room Entity (Medicine Schema)
│   ├── NotificationHistory.kt   # Room Entity (Action Logs)
│   ├── MedicineDao.kt           # Data Access Object (SQL queries)
│   ├── MedicineDatabase.kt      # Room DB Singleton Manager
│   └── MedicineRepository.kt    # Repository Abstracting local data
├── viewmodel/
│   └── MedicineViewModel.kt     # ViewModel managing LiveData streams
├── adapter/
│   ├── MedicineAdapter.kt       # ListAdapter with DiffUtil
│   └── HistoryAdapter.kt        # ListAdapter for action logs
├── ui/ (Activities)
│   ├── MainActivity.kt          # Dashboard & Observers
│   ├── AddMedicineActivity.kt   # Input & Date/Time pickers
│   ├── LoginActivity.kt / SignUpActivity.kt  # Auth & SharedPreferences
│   └── SplashActivity.kt        # Entry & Animations
└── receivers/
    ├── AlarmReceiver.kt         # Core alarm generation & wake lock
    ├── ActionReceiver.kt        # User notification response handling
    └── BootReceiver.kt          # Alarm regeneration on device boot
```

---

## 🚀 Future Scope

MediCare+ provides a solid foundation, but there is immense potential for expansion to make it a globally accessible, comprehensive health assistant:

1. **🌍 Multiple Language Support (Localization):**
   - Translate strings into global and regional languages (Spanish, Hindi, Mandarin, etc.) using Android's native `res/values-xx/strings.xml` framework to reach a wider demographic, particularly elderly users who may not be comfortable with English.
2. **🏥 Direct Hospital System Integration:**
   - Implement REST APIs (using Retrofit) to link directly with Health Information Systems (HIS). 
   - Allow prescriptions to be synced automatically from the hospital's database directly into the app, eliminating manual entry.
   - Enable "SOS" or direct calling functionalities from the app directly to the saved hospital reception.
3. **☁️ Cloud Sync & Authentication:**
   - Migrate from local `SharedPreferences` to Firebase Authentication.
   - Sync Room Database via Firebase Firestore so users don't lose their data when switching devices.
4. **📊 Analytics & Refill Reminders:**
   - Provide graphical charts on adherence streaks.
   - Track pill inventory and notify the user when it's time to request a refill from the pharmacy.

---

## 💻 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11+
- Android SDK 35

### Running the Project

1. **Clone the repository**
   ```bash
   git clone https://github.com/omkardev07/MediCarePlus.git
   ```
2. **Open in Android Studio** and wait for Gradle to sync (uses Gradle 8.10.2).
3. **Run on a physical device or emulator**. 
   *(Note: Testing alarms is strongly recommended on a physical device to observe accurate Doze mode and WakeLock behavior).*

---

## 👨‍💻 Developer

<p align="center">
  <b>Omkar Ganesh Kute</b><br>
  <a href="https://github.com/omkardev07">
    <img src="https://img.shields.io/badge/GitHub-omkardev07-181717?style=for-the-badge&logo=github"/>
  </a>
</p>

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Made with ❤️ by <b>Omkar</b> · Built for better health 🌟
</p>
