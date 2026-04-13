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
  <img src="https://img.shields.io/badge/Min%20SDK-24-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge"/>
</p>

---

## 📖 About

**MediCare+** is a feature-rich Android application designed to help users stay on top of their medication schedules. It provides intelligent reminders with auto-snooze and retry logic, ensuring users never miss a dose. Built with a premium, Apple Health-inspired UI and powered by modern Android development practices.

> Never miss a dose again — MediCare+ keeps you healthy, on time, every time.

---

## ✨ Features

### 💊 Medicine Management
- **Add, Edit & Delete** medicines with name, dosage, and schedule
- **Before/After Meal** toggle for each medicine
- **Repeat Options** — Daily, Weekly, or One-time reminders
- **Date & Time Picker** with future-only validation

### ⏰ Smart Alarm System
- **Exact Alarms** using `AlarmManager.setExactAndAllowWhileIdle()` for reliability
- **3-Retry Auto-Snooze** — If you don't respond, it reminds you again (up to 3 times)
- **YES / NO Actions** directly from the notification
- **Boot Persistence** — Alarms automatically reschedule after device reboot
- **Wake Lock Support** — Ensures alarms fire even in deep sleep

### 🔔 Premium Notifications
- **High-priority alarm notifications** with sound and vibration
- **Full-screen intent** support to wake the screen
- **Personalized messages** with user's name and motivational health tips
- **Missed medicine alerts** after all retries are exhausted

### 🏥 Hospital Connect
- Save your **hospital name, branch, phone number**, and **doctor name**
- Hospital info is linked to medicine reminders for quick access

### 👨‍👩‍👧 Family Connect
- Store up to **2 emergency family contacts** with name, number, and relation
- Family contact info is included in alarm notifications

### 📋 Notification History
- Full log of all medicine actions: **Taken**, **Missed**, **Snoozed**
- Timestamped entries with color-coded status indicators

### 🔐 Authentication
- **Sign Up / Login** system with local SharedPreferences storage
- **"Keep Me Logged In"** checkbox for persistent sessions
- Full name support for personalized welcome greetings

### 🎨 Premium UI/UX
- **Apple Health-inspired** clean, vibrant design
- **Smooth animations** — fade-ins, scale pops, staggered reveals
- **Glassmorphism cards** and soft pastel feature tiles
- **Dynamic health tips** on the home screen
- **Material Design 3** components throughout

---

## 🏗️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin |
| **UI** | XML Layouts + ViewBinding |
| **Database** | Room (SQLite) with KSP |
| **Architecture** | MVVM (ViewModel + LiveData + Repository) |
| **Async** | Kotlin Coroutines + Flow |
| **Alarms** | AlarmManager + BroadcastReceiver |
| **Notifications** | NotificationCompat + NotificationChannel |
| **DI** | Manual (ViewModel factory pattern) |
| **Build** | Gradle 8.10.2 + AGP 8.7.3 |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 35 (Android 15) |

---

## 📁 Project Structure

```
app/src/main/java/com/example/medicareplus/
├── data/
│   ├── Medicine.kt              # Room Entity — medicine data model
│   ├── MedicineDao.kt           # DAO — database operations
│   ├── MedicineDatabase.kt      # Room Database singleton
│   ├── MedicineRepository.kt    # Repository — data access layer
│   └── NotificationHistory.kt   # Room Entity — notification log
├── adapter/
│   ├── MedicineAdapter.kt       # RecyclerView adapter for medicines
│   └── HistoryAdapter.kt        # RecyclerView adapter for history
├── viewmodel/
│   └── MedicineViewModel.kt     # ViewModel — UI state management
├── MainActivity.kt              # Home screen with medicine list
├── AddMedicineActivity.kt       # Add/Edit medicine form
├── LoginActivity.kt             # User login screen
├── SignUpActivity.kt            # User registration screen
├── SplashActivity.kt            # Animated splash screen
├── HospitalActivity.kt          # Hospital info management
├── FamilyActivity.kt            # Family contacts management
├── HistoryActivity.kt           # Notification history viewer
├── AlarmReceiver.kt             # Alarm broadcast handler
├── ActionReceiver.kt            # Notification action handler
└── BootReceiver.kt              # Boot-completed alarm rescheduler
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11+
- Android SDK 35

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/omkardev07/MediCarePlus.git
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the MediCarePlus folder
   ```

3. **Sync Gradle** — Android Studio will auto-download dependencies

4. **Run on device/emulator**
   ```
   Click ▶️ Run or press Shift + F10
   ```

### Permissions Required
| Permission | Purpose |
|-----------|---------|
| `SCHEDULE_EXACT_ALARM` | Reliable medicine reminders |
| `POST_NOTIFICATIONS` | Showing alarm notifications |
| `RECEIVE_BOOT_COMPLETED` | Rescheduling alarms after reboot |
| `VIBRATE` | Notification vibration |
| `WAKE_LOCK` | Keeping device awake for alarms |
| `USE_FULL_SCREEN_INTENT` | Waking screen for urgent reminders |

---

## 📱 App Screens

| Screen | Description |
|--------|-------------|
| 🎬 **Splash** | Animated logo with pulse effect |
| 🔐 **Login** | Clean login with shake-on-error animation |
| 📝 **Sign Up** | User registration with validation |
| 🏠 **Home** | Medicine list, health tips, feature cards |
| 💊 **Add Medicine** | Full medicine form with date/time picker |
| 🏥 **Hospital** | Save hospital & doctor details |
| 👨‍👩‍👧 **Family** | Emergency contact management |
| 📋 **History** | Color-coded notification action log |

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

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
