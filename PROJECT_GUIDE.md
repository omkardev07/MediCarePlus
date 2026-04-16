# PROJECT_GUIDE

## SECTION 1: PROJECT INTRODUCTION

**Project Name:** MediCare+ (Medicine Reminder App)

**What this app does:**
- Helps people remember to take their medicines on time.
- Sends a notification at the exact medicine time.
- Notification has two buttons: YES (I took it) and NO (I missed/snooze).
- If user misses a dose or clicks NO multiple times, the app automatically sends an emergency SMS to a doctor or family member.
- All medicine data is stored locally on the phone (no internet needed).

**Why we built it:**
Many people, especially elderly and busy patients, forget to take medicines. Missing doses can harm health. This app solves that problem.

---

## SECTION 2: COMPLETE FILE STRUCTURE (Where everything is located)

When you open Android Studio and look at the left panel, here is the exact structure:

```
MediCare+ (project root)
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/medicineenrender/
│   │   │   │   ├── LoginActivity.kt
│   │   │   │   ├── SignUpActivity.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── AddMedicineActivity.kt
│   │   │   │   ├── HistoryActivity.kt
│   │   │   │   ├── EmergencySettingsActivity.kt
│   │   │   │   ├── AlarmNotificationActivity.kt
│   │   │   │   ├── AlarmReceiver.kt
│   │   │   │   ├── NotificationActionReceiver.kt
│   │   │   │   ├── BootReceiver.kt
│   │   │   │   ├── NotificationHelper.kt
│   │   │   │   ├── SmsHelper.kt
│   │   │   │   ├── Reminder.kt (database entity)
│   │   │   │   ├── MedicineDao.kt (database queries)
│   │   │   │   └── MedicineDatabase.kt (database setup)
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_login.xml
│   │   │   │   │   ├── activity_signup.xml
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_add_medicine.xml
│   │   │   │   │   ├── activity_history.xml
│   │   │   │   │   ├── activity_emergency_settings.xml
│   │   │   │   │   └── activity_alarm_notification.xml
│   │   │   │   ├── drawable/ (icons, button shapes)
│   │   │   │   ├── values/ (colors, strings, themes)
│   │   │   │   └── values-night/ (dark mode colors)
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts (project level)
├── settings.gradle.kts
└── local.properties
```

---

## SECTION 3: HOW TO OPEN EACH IMPORTANT FILE IN ANDROID STUDIO

Follow these steps exactly:

1. Open Android Studio and click "Open" – select the MediCare+ folder.
2. On the left side, you see "Project" panel. Click the small arrow to expand folders:
   - Click `app` → `src` → `main` → `java` → `com.example.medicineenrender`
   - Now you see all `.kt` files. Double-click any to open its code.
3. To open XML layout files (screen designs):
   - Go to `app` → `src` → `main` → `res` → `layout`
   - Double-click any `.xml` file (like `activity_main.xml`).
4. To open the manifest file (permissions list):
   - In the Project panel, scroll down to `AndroidManifest.xml` (it's right under `res` folder or at same level). Double-click.
5. To open Gradle files (build settings):
   - For app-level settings: `app` → `build.gradle.kts`
   - For project-level settings: `build.gradle.kts` (the one at the very top, outside app folder)

---

## SECTION 4: LIST OF KOTLIN FILES AND WHAT EACH DOES

Here are the main Kotlin files and their purpose (in simple words):

| File Name | What it does |
|-----------|---------------|
| LoginActivity.kt | Shows login screen. Checks username/password. Has fingerprint login option. |
| SignUpActivity.kt | Creates a new user account. Stores username and password. |
| MainActivity.kt | Home screen after login. Shows a health quote, a button to add new medicine, and a list of upcoming medicines. |
| AddMedicineActivity.kt | Form to enter medicine name, dosage (e.g., "1 tablet"), date, time, and repeat option. Saves to database. |
| HistoryActivity.kt | Shows a list of all medicine actions – Taken, Missed, or Snoozed – with timestamps. |
| EmergencySettingsActivity.kt | Allows user to enter doctor and family phone numbers, set delay time, and test SMS. |
| AlarmNotificationActivity.kt | A full-screen screen that appears when user taps the notification. Shows big YES/NO buttons. |
| AlarmReceiver.kt | Triggered by the alarm at the scheduled medicine time. Creates and shows the notification. |
| NotificationActionReceiver.kt | Handles clicks on YES/NO buttons inside the notification. Updates the database. |
| BootReceiver.kt | When phone restarts, this reschedules all missed alarms so reminders still work. |
| NotificationHelper.kt | Helper file that builds the notification (title, text, icons, sound, vibration). |
| SmsHelper.kt | Sends SMS messages using the phone's SMS manager. |
| Reminder.kt | Defines the database table structure: columns like id, medicine name, dosage, time, status. |
| MedicineDao.kt | Contains SQL commands (insert, delete, get all medicines). |
| MedicineDatabase.kt | Sets up the Room database and provides access to the DAO. |

---

## SECTION 5: LIST OF XML LAYOUT FILES (Screen Designs)

| XML File | Which screen it designs |
|----------|-------------------------|
| activity_login.xml | Login screen |
| activity_signup.xml | Sign-up screen |
| activity_main.xml | Home screen |
| activity_add_medicine.xml | Add medicine form |
| activity_history.xml | History list screen |
| activity_emergency_settings.xml | Emergency contacts screen |
| activity_alarm_notification.xml | Full-screen YES/NO confirmation screen |
| item_medicine.xml | Single row design for medicine list |
| item_history.xml | Single row design for history list |

---

## SECTION 6: DATABASE EXPLANATION (Room + SQLite)

**What is Room?**  
Room is a library from Google that makes it easy to save data on the phone. It uses SQLite (a small database inside the phone).

**Where is the data stored?**  
Inside the phone's internal memory. No internet needed. Data stays even if you close the app or restart the phone.

**Three parts of Room Database:**

1. **Entity** (`Reminder.kt`) – This defines what a medicine record looks like. Example: each medicine has an ID, name, dosage, time, and status (Taken/Missed).

2. **DAO** (`MedicineDao.kt`) – This is the "Data Access Object". It contains commands like:
   - `insert` – to add a new medicine
   - `delete` – to remove a medicine
   - `getAll` – to fetch all medicines
   - `update` – to change status (Taken/Missed)

3. **Database** (`MedicineDatabase.kt`) – This creates the actual database file and gives you access to the DAO.

**How data flows:**  
When you add a medicine → `AddMedicineActivity` calls `MedicineDao.insert()` → Room saves it to SQLite → later when you open history, it calls `getAll()` and shows the list.

---

## SECTION 7: GRADLE EXPLANATION (Build System)

**What is Gradle?**  
Gradle is a tool that automatically builds your app. It does three main jobs:
- Downloads required libraries (like Room, Biometric, etc.) from the internet.
- Compiles your Kotlin code and XML files into an APK.
- Manages app version and Android SDK versions.

**Two important Gradle files:**

1. **Project-level build.gradle.kts** (in the root folder)  
   - Tells Android which plugins to use (Android plugin, Kotlin plugin).

2. **App-level build.gradle.kts** (inside `app` folder)  
   - Defines `minSdk` (minimum Android version – we use API 24 = Android 7.0)
   - Defines `targetSdk` (Android 14)
   - Lists all dependencies: `implementation "androidx.room:room-runtime:2.6.0"`, etc.

**How to sync Gradle:**  
After changing any dependency, click `File` → `Sync Project with Gradle Files`. Gradle downloads everything.

---

## SECTION 8: APK EXPLANATION (Android Package Kit)

**Full form:** Android Package Kit (or Android Application Package).

**What is inside an APK?**  
- Your compiled Kotlin code
- All XML layouts and images
- The AndroidManifest.xml file
- A digital signature

**How to generate an APK from Android Studio:**  
1. Click `Build` → `Build Bundle(s) / APK` → `Build APK`
2. Wait for the build to finish (a popup appears)
3. Click `locate` – the APK is saved in `app/build/outputs/apk/debug/`

**How to install the APK on a mobile phone:**  
1. Copy the `.apk` file to your phone (via USB cable, email, WhatsApp, or Google Drive)
2. On the phone, open a file manager and tap the `.apk` file
3. If asked, allow "Install from unknown sources" (Settings → Security → Unknown sources)
4. Tap "Install" – the app appears in your app drawer

---

## SECTION 9: PERMISSIONS IN ANDROIDMANIFEST.XML

Here are all permissions used in this app and why:

| Permission | Why we need it |
|------------|----------------|
| POST_NOTIFICATIONS | To show medicine reminder notifications (required for Android 13+) |
| SCHEDULE_EXACT_ALARM | To set exact alarm time (required for Android 12+) |
| USE_BIOMETRIC | To allow fingerprint login |
| SEND_SMS | To send emergency SMS to doctor/family |
| VIBRATE | To make phone vibrate when notification arrives |
| USE_FULL_SCREEN_INTENT | To show the YES/NO screen on lock screen |
| RECEIVE_BOOT_COMPLETED | To restart alarms after phone reboot |

---

## SECTION 10: HOW NOTIFICATIONS WORK (Step by Step)

1. User adds a medicine and sets a time (e.g., 10:00 AM).
2. The app calculates how many milliseconds from now until 10:00 AM.
3. The app uses `AlarmManager` to schedule an alarm at that exact time.
4. At 10:00 AM, Android launches `AlarmReceiver`.
5. `AlarmReceiver` creates a notification using `NotificationHelper` and shows it on the screen.
6. The notification has two action buttons: YES and NO. Each button is linked to `NotificationActionReceiver`.
7. If user clicks YES:  
   - `NotificationActionReceiver` marks the medicine status as "Taken" in the database.
   - The notification disappears.
8. If user clicks NO:  
   - `NotificationActionReceiver` marks the medicine as "Missed" or "Snoozed".
   - If missed multiple times, it triggers `SmsHelper` to send an SMS to emergency contacts.
9. If the phone is restarted, `BootReceiver` runs and reschedules all pending alarms.

---

## SECTION 11: HOW TO BUILD AND RUN THE APP (On a Real Phone)

**Method 1: Using Android Studio (easiest)**
1. Connect your Android phone to the computer with a USB cable.
2. On the phone, enable "Developer options" and "USB debugging" (Settings → Developer options).
3. In Android Studio, click the green Run button (▶️) at the top.
4. Select your phone from the list.
5. Android Studio builds the APK, installs it, and opens the app automatically.

**Method 2: Install APK directly (without Android Studio)**
1. Generate an APK (as described in Section 8).
2. Transfer the APK to your phone.
3. Tap the APK file and install.

---

## SECTION 12: TOOLS AND TECHNOLOGIES USED

| Tool | What we used it for |
|------|----------------------|
| Android Studio | Writing code, designing screens, building APK |
| Kotlin | Programming language for app logic |
| XML | Designing user interface (buttons, text, layouts) |
| Room Database | Storing medicine data locally on the phone |
| AlarmManager | Scheduling medicine reminders at exact times |
| NotificationManager | Showing notifications and handling YES/NO buttons |
| BroadcastReceiver | Listening for alarm events and phone reboot events |
| Biometric API | Fingerprint login |
| SmsManager | Sending emergency SMS |
| Git & GitHub | Saving code online and version control |
| AI tools (ChatGPT, Claude, Antigravity, Gemini) | Helping write code, fix errors, and create documentation |

---

## SECTION 13: FUTURE IMPROVEMENTS (What can be added later)

- Cloud backup – sync medicine data across multiple devices.
- Smartwatch support – get reminders directly on wrist.
- Voice reminders in Marathi, Hindi, and other languages.
- Machine learning to predict which doses are most likely to be missed.
- Doctor dashboard – send monthly adherence reports to doctors.

---

## SECTION 14: SUMMARY OF THE APP

**What the user experiences:**
1. Opens app → logs in with fingerprint or password.
2. Adds medicine name, dosage, time.
3. At that time, a loud notification appears with YES/NO buttons.
4. Press YES → history shows "Taken".
5. Press NO or ignore → after some time, SMS goes to doctor/family.
6. All data stays on phone – private and offline.

**Why this app is useful:**
- Never forget medicines again.
- Emergency SMS provides safety for elderly or seriously ill patients.
- Easy to use – big buttons, simple design.
- Works without internet.
