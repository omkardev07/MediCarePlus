# 💊 MediCare+ — Medicine Reminder App

A modern, feature-rich medicine reminder app built with **Kotlin** and **Material Design 3** for Android.

> Never miss your medicine again! MediCare+ helps you stay on track with smart reminders, notification history, and emergency alerts.

---

## 📥 Download

### 🆕 Latest Release — v2.0

[![Download APK](https://img.shields.io/badge/DOWNLOAD-APK_v2.0-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://github.com/omkardev07/MediCarePlus/raw/master/MediCarePlus-App.apk)

👉 **[Download MediCare+ v2.0 Source Code (ZIP)](https://github.com/omkardev07/MediCarePlus/archive/refs/heads/master.zip)**

**Previous Releases**

| Version | Download |
|---------|----------|
| v1.0 | [MediCare-Plus-v1.0.zip](#) |

---

## ✨ Features

### 🔐 Secure Login
* Username & password authentication
* **Fingerprint / biometric login** support
* "Keep me logged in" option
* Secure sign-up with password validation

### 💊 Medicine Management
* **Add, edit, and delete** medicine reminders
* Set **dosage** (e.g., "1 tablet", "5ml syrup")
* Schedule reminders for a **specific time**
* **Repeat daily** or choose **specific days** (Mon, Wed, Fri, etc.)
* One-time reminder support

### 🔔 Smart Notifications
* Rich notifications with **medicine name, dosage, and time**
* **YES / NO action buttons** directly in the notification
* Tap notification to open **in-app confirmation screen** with color-coded buttons
* Auto-dismiss notifications after interaction
* Missed medicine detection

### 📋 Notification History
* Complete log of all medicine interactions
* Shows **taken** ✅ or **missed** ❌ status
* Timestamps for every action
* Dosage information in history

### 🚨 Emergency SMS Alerts
* Set up **primary and secondary emergency contacts**
* If a medicine is missed after a configurable delay, **SMS alerts** are sent automatically
* Customizable delay (default: 30 minutes)
* Test SMS feature to verify setup
* Emergency notification on device

### 🌙 Dark Mode
* Full **dark mode support** that follows system theme
* Custom dark color palette with proper contrast
* Dark gradient headers
* Works automatically — no manual toggle needed

### 💬 Health Motivation
* **Random health quotes** on the main screen
* Motivational messages throughout the app
* Emoji-rich, friendly UI

---

## 📱 Compatibility

| Requirement | Value |
|-------------|-------|
| **Minimum Android** | Android 7.0 (API 24) |
| **Target Android** | Android 14 (API 34) |
| **Supported Devices** | Android 7.0 — Android 14+ |
| **Architecture** | ARM64, ARM, x86_64, x86 |

---

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI:** Material Design 3, ViewBinding
* **Architecture:** Activity-based with Shared Preferences / Room (SQLite)
* **Notifications:** AlarmManager + NotificationManager + BroadcastReceiver
* **Auth:** AndroidX Biometric API
* **SMS:** Android SmsManager
* **Build:** Gradle 8.10.2, Android Gradle Plugin

---

## 🏗 Build from Source

### Prerequisites
1. Android Studio Ladybug or newer
2. JDK 17 (embedded in Android Studio)
3. Android SDK Platform 34

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/omkardev07/MediCarePlus.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Click **Run** (`Shift + F10`) to build and deploy to your emulator or physical device.

---

## 📂 Project Structure

```text
app/src/main/
├── java/com/example/medicineenrender/
│   ├── LoginActivity.kt              # Login + biometric auth
│   ├── SignUpActivity.kt             # User registration
│   ├── MainActivity.kt               # Dashboard + reminder list
│   ├── AlarmReceiver.kt              # Handles alarm broadcasts
│   ├── NotificationHelper.kt         # Creates rich notifications
│   ├── NotificationActionReceiver.kt # YES/NO button handler
│   ├── AlarmNotificationActivity.kt  # In-app confirmation
│   ├── HistoryActivity.kt            # History screen
│   ├── EmergencySettingsActivity.kt  # Emergency contacts
│   ├── Reminder.kt                   # Data model
│   └── SmsHelper.kt                  # SMS sending utility
└── res/
    ├── layout/                       # XML layouts
    ├── values/                       # Colors, themes, strings
    ├── values-night/                 # Dark mode colors & themes
    ├── drawable/                     # Gradients, icons
    └── menu/                         # Toolbar menu
```

---

## 📸 Screenshots

<p align="center">
  <img src="./assets/login.png" width="22%" />
  <img src="./assets/home.png" width="22%" />
  <img src="./assets/add.png" width="22%" />
  <img src="./assets/hospital.png" width="22%" />
</p>

---

## 🔑 Permissions

| Permission | Purpose |
|------------|---------|
| `POST_NOTIFICATIONS` | Show medicine reminders |
| `SCHEDULE_EXACT_ALARM` | Schedule precise alarm times |
| `USE_BIOMETRIC` | Fingerprint login |
| `SEND_SMS` | Emergency alerts to contacts |
| `VIBRATE` | Notification vibration |
| `USE_FULL_SCREEN_INTENT` | Show alarm on lock screen |

---

## 📦 Releases & Changelog

### v2.0 — March 17, 2026

* 🔄 Second release tag pushed to GitHub
* 🐛 Bug fixes and stability improvements
* 📦 Updated APK build (`MediCarePlus-App.apk`)

### v1.0 — Initial Release

* 🚀 First public release of MediCare+
* 🔐 Biometric login support
* 💊 Medicine reminders with dosage & schedule
* 🔔 Rich notifications with YES / NO actions
* 📋 Notification history (taken / missed)
* 🚨 Emergency SMS alerts to contacts
* 🌙 Full dark mode support
* 💬 Health motivation quotes

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

---

## 📄 License

This project is open source and available under the [MIT License](https://opensource.org/licenses/MIT).

<br/>
<p align="center">
  Made by <a href="https://github.com/omkardev07">Omkar Kute</a>
</p>
