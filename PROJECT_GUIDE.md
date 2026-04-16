<div align="center">
  <img src="https://img.icons8.com/color/150/000000/pill.png" alt="MediCare+ Logo" width="120" />
  <h1>MediCare+ 💊</h1>
  <p><b>Your Personal & Smart Medicine Reminder App</b></p>
  
  ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
  ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
  ![Room](https://img.shields.io/badge/Room_Database-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)
</div>

<br/>

> **MediCare+** solves a critical real-world problem: helping busy patients and the elderly remember their medication. With smart notifications, offline databases, and emergency SMS alerts, MediCare+ ensures no dose is ever missed.

---

## 🚀 Quick Download

### Experience the App instantly!
You can install the application directly on your Android phone without needing Android Studio.

📥 **[Download MediCare+ APK](https://github.com/omkardev07/MediCarePlus/raw/master/MediCarePlus-App.apk)**

> _Note: You may need to enable "Install from Unknown Sources" on your Android device to install the APK._

---

## 🌟 1. Project Introduction

**What this app does:**
- 🔔 **Timely Reminders:** Sends an exact, full-screen notification when it's time for medicine.
- ✅ **Actionable Alerts:** Notification has two simple buttons: **YES** (I took it) and **NO** (I missed/snooze).
- 🚨 **Emergency SMS:** If a user repeatedly misses a dose or clicks NO, the app auto-sends an emergency SMS to a doctor or family member.
- 📴 **100% Offline:** All medicine data is stored locally via Room SQLite Database (No internet required, secure for privacy).

**Why we built it:**
Missing medication doses can seriously harm patient health. We built MediCare+ to create a simple, foolproof, and reliable safety net for everyday people. 

---

## 📂 2. Complete File Structure

<details>
<summary><b>👉 Click here to reveal the Android Studio Project Structure</b></summary>
<br/>
When you open Android Studio, the project looks like this:

```text
MediCare+ (Project Root)
│
├── app/
│   ├── src/main/
│   │   ├── java/com/example/medicineenrender/
│   │   │   ├── LoginActivity.kt
│   │   │   ├── SignUpActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── AddMedicineActivity.kt
│   │   │   ├── HistoryActivity.kt
│   │   │   ├── EmergencySettingsActivity.kt
│   │   │   ├── AlarmNotificationActivity.kt
│   │   │   ├── AlarmReceiver.kt
│   │   │   ├── NotificationActionReceiver.kt
│   │   │   ├── BootReceiver.kt
│   │   │   ├── NotificationHelper.kt
│   │   │   ├── SmsHelper.kt
│   │   │   ├── Reminder.kt (Database Entity)
│   │   │   ├── MedicineDao.kt (Database Queries)
│   │   │   └── MedicineDatabase.kt (Database Setup)
│   │   ├── res/
│   │   │   ├── layout/ (All .xml UI screens)
│   │   │   ├── drawable/ (Icons & Shapes)
│   │   │   ├── values/ & values-night/ (Colors, Strings, Themes)
│   │   ├── AndroidManifest.xml
│   └── build.gradle.kts (App level)
├── build.gradle.kts (Project level)
└── settings.gradle.kts
```
</details>

---

## 💻 3. App Architecture & Core Files

We mapped out our code cleanly. Here is exactly what runs the application:

### 🧩 Kotlin Files (The Brains)

| File Name | Functional Purpose |
|:---|:---|
| **`LoginActivity.kt`** | Handles user authentication, including modern **Fingerprint/Biometric login**. |
| **`MainActivity.kt`** | The central dashboard displaying health quotes and the upcoming medication schedule. |
| **`AddMedicineActivity.kt`** | The input form for medicine details (name, dosage, date, specific time). |
| **`HistoryActivity.kt`** | A timeline of all actions taken by the user *(Taken, Missed, or Snoozed)*. |
| **`EmergencySettingsActivity.kt`**| Where users define the SOS contact number and notification delay settings. |
| **`AlarmNotificationActivity.kt`**| A specialized **Full-Screen Intent** that awakens the screen during lock state. |
| **`AlarmReceiver.kt`** | Intercepts OS-level alarm triggers and fires our visual notifications. |
| **`BootReceiver.kt`** | Automatically reschedules all pending alarms if the phone reboots. |

### 🎨 XML Layout Files (The Looks)

> These reside in `app/src/main/res/layout/` and dictate our beautiful user interfaces.

* `activity_login.xml` & `activity_signup.xml` - Authentication UI.
* `activity_main.xml` - Dashboard UI.
* `activity_add_medicine.xml` - Clean input form.
* `activity_alarm_notification.xml` - The giant **YES / NO** screen layout.
* `item_medicine.xml` & `item_history.xml` - Sub-components for our dynamic list Recycler Views.

---

## 💾 4. Database Architecture (Room + SQLite)

To ensure privacy and offline capabilities, we used Google's **Room Database** architecture.

* 📦 **Entity (`Reminder.kt`):** The blueprint. Defines the table rows (ID, Medicine Name, Time, Current Status).
* 🛠 **DAO (`MedicineDao.kt`):** The engine. Contains SQL instructions like `@Insert`, `@Delete`, and `@Query("SELECT * FROM reminders")`.
* 🏦 **Database (`MedicineDatabase.kt`):** The manager. Compiles the Entity and DAO into a functional SQLite file running hidden on the phone.

---

## ⚙️ 5. Build System & Compilation

We use **Gradle (Kotlin DSL)** to orchestrate our build process:

1. **`build.gradle.kts` (Project Level):** Configures overarching plugins.
2. **`build.gradle.kts` (App Level):** 
   * Sets our target Android 14 compatibility.
   * Compiles external libraries via `implementation` tags (e.g., Room, Biometrics).
3. **APK Generation:** Gradle compiles all of the above `.kt` logic, `.xml` designs, and library assets into one installable `.apk` file.

<details>
<summary><b>👉 How to view files in Android Studio</b></summary>

1. Open Android Studio → Expand **`app`** folder.
2. Code: Navigate to `src` → `main` → `java`.
3. Design: Navigate to `src` → `main` → `res` → `layout`.
4. Gradle: Open `build.gradle.kts` located at the very bottom of the directory view.
</details>

---

## ⚙️ 6. App Permissions (`AndroidManifest.xml`)

Security and user trust are paramount. We request precise permissions:

| Permission Request | Real-World Application |
|:---|:---|
| `<uses-permission android:name="POST_NOTIFICATIONS" />` | Essential for Android 13+ to display the alarm. |
| `<uses-permission android:name="SCHEDULE_EXACT_ALARM" />` | Precision timing for medication. |
| `<uses-permission android:name="USE_BIOMETRIC" />` | Unlocks the app via Fingerprint. |
| `<uses-permission android:name="SEND_SMS" />` | Core feature: automatic emergency dispatch. |
| `<uses-permission android:name="USE_FULL_SCREEN_INTENT" />`| Ensures the alarm wakes up locked phones. |

---

## 🔔 7. The Notification Engine: Step-by-Step

Our most complex logic handles the reminders:

1. User saves a medicine at **10:00 AM**.
2. App calculates the exact millisecond differential and gives it to Android's `AlarmManager`.
3. Exactly at 10:00 AM, the OS triggers our `AlarmReceiver.kt`.
4. `NotificationHelper` builds a high-priority, vibrating UI with **YES** and **NO** action intents.
5. If **YES** is tapped: `NotificationActionReceiver` tags the record as _Taken_ in Room DB.
6. If **NO** is tapped repeatedly: `SmsHelper` silently executes an SOS SMS to the emergency contacts.

---

## 🔮 8. Future Roadmap

We built a scalable foundation. Here is what we plan to do next:
* ☁️ **Cloud Synchronization:** Sync databases securely using Firebase.
* ⌚ **WearOS Integration:** Dismiss alarms directly from a Smartwatch.
* 🗣 **Multilingual Voice Prompts:** Audio announcements in Marathi, Hindi, & English.
* 🧠 **AI Suggestions:** Predictive models guessing when a user is most likely to miss a dose based on history.

---

<div align="center">
  <b>Built with ❤️ for Health and Safety.</b><br/>
  <i>Everything stays on your phone, completely private, completely offline.</i>
</div>
