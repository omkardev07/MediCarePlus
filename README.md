<p align="center">
  <img src="assets/banner.png" alt="MediCare+ Banner" width="100%"/>
</p>

# 💊 MediCare+ : Premium Medication Assistant
**Your health, simplified.** — A smart Android application designed to eliminate medication non-adherence through intelligent scheduling, auto-snooze alarms, and an integrated health ecosystem.

<div align="center">
  
[![Download APK](https://img.shields.io/badge/Download_App-Android_APK-3DDC84?style=for-the-badge&logo=android&logoColor=white)](./MediCarePlus-App.apk)
  
</div>

<p align="center">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Database-Room_SQLite-007AFF?style=flat-square&logo=sqlite&logoColor=white"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF5722?style=flat-square"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-orange?style=flat-square"/>
</p>

---

## 🎯 The Problem

Medication non-adherence is a major global health challenge. Patients—particularly the elderly and those managing chronic conditions—frequently forget to take their medications on time. Conventional app alarms are easily dismissed and forgotten, resulting in missed doses, worsened health outcomes, and unnecessary hospital visits. Furthermore, managing disjointed health information (prescriptions, hospital details, emergency contacts) is cumbersome.

## 💡 The Solution

**MediCare+** was engineered to solve this by acting as a persistent and comprehensive health assistant. 

Unlike standard alarms, MediCare+ utilizes a robust **Auto-Snooze & Retry Engine**. If an alarm is dismissed or ignored, the app automatically reschedules a fallback reminder until the user actively confirms they have taken the dose. It unites this failsafe reminder system with emergency family contacts and hospital information in a single, premium interface.

---

## ✨ Key Features

### ⏱️ Persistent Smart Alarms
- **100% Reliable Scheduling:** Uses exact alarms and Android `WakeLocks` to bypass deep sleep modes.
- **3-Retry Auto-Snooze:** The core engine acts as a safety net. If you ignore the alarm, it rings again up to three times.
- **Interactive Actions:** Directly confirm "Taken" or choose to "Snooze" straight from the notification.
- **Reboot Resilient:** Advanced `BootReceiver` restores all impending alarms if the phone is restarted.

### 💊 Complete Medication Management
- Log detailed prescriptions, accurate dosages, and critical dietary instructions ("Before/After Meal").
- Schedule complex recurring routines (Daily, Weekly, One-Time).
- View a detailed, color-coded **Notification History** to audit adherence.

### 🏥 Integrated Health Context
- **Hospital Connect:** Locally caches the hospital branch, phone number, and doctor details so they are one tap away during an emergency.
- **Family Connect:** Links up to two emergency family members directly to your medical reminders.

---

## 🏗️ Technical Architecture

This application models modern enterprise Android standards, ensuring scalability, maintainability, and clean code separation.

| Layer / Technology | Implementation Description |
|-------------------|----------------------------|
| **UI Layer** | Written with standard XML, leveraging **ViewBinding** for precise type-safety and **Material Design 3** for glassmorphic elements and high-end aesthetics. |
| **Presentation** | Implements the **MVVM (Model-View-ViewModel)** architectural pattern. Resolves configuration changes dynamically. |
| **Data Layer** | Backed by **Room Database** utilizing `KSP` processors for localized, offline-first SQLite storage. |
| **Concurrency** | Utilizes **Kotlin Coroutines and Flow** to execute non-blocking, asynchronous database updates and UI emissions. |
| **Background execution** | Relies entirely on optimized `BroadcastReceivers` combined with `AlarmManager` instead of battery-draining background services. |

---

## 🚀 Future Scope & Scale

The foundational architecture of MediCare+ supports massive future improvements. Upcoming roadmap goals include:

- **🌍 Native Multi-language Support:** Scaling to support regional languages to assist elderly users globally.
- **🏥 Direct Hospital API Integration:** Implementing REST API sync using Retrofit to automatically download prescriptions from Health Information Systems (HIS) and eliminating manual entry.
- **☁️ Cloud Sync via Firebase:** Transitioning from local SQLite persistence to Firebase Firestore for cross-device authentication and real-time syncing.

---

## 💻 Installation & Setup

You can download the compiled App directly: **[Download MediCare+ APK](./MediCarePlus-App.apk)**

**To run the project locally:**
1. Clone the repository:
   ```bash
   git clone https://github.com/omkardev07/MediCarePlus.git
   ```
2. Open the project in **Android Studio Hedgehog** (uses Gradle 8.10.2).
3. Connect a physical Android device or Emulator.
4. Click **Run**. 
*(Note: To test exact alarms properly, a physical device is recommended over emulators missing Doze simulations).*

---

## 👨‍💻 Developer Profile

<p align="center">
  <b>Omkar Ganesh Kute</b><br>
  <a href="https://github.com/omkardev07">
    <img src="https://img.shields.io/badge/GitHub-omkardev07-181717?style=for-the-badge&logo=github"/>
  </a>
</p>

---

<p align="center">
  <b>MediCare+</b> • Made with ❤️ by Omkar to simplify better health.
</p>
