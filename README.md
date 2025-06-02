# 🩺 Daily Health Tracker

A Kotlin-based Android app to help users track and visualize their daily health metrics including water intake, step count, sleep hours, mood, and weight. Built with modern Android tools like Jetpack Compose, Firebase, and MPAndroidChart.

---

## 📱 Features

### 🏠 Home Dashboard
- Displays today’s health summary: water intake, steps, sleep, mood, and weight.
- Quick access to adding or editing today’s entry.

### 🧾 Daily Entry
- Input the following:
  - 💧 Water intake (ml/cups)
  - 💤 Sleep hours
  - 👣 Steps (manual or via Google Fit)
  - 😊 Mood (emoji scale)
  - ⚖️ Weight (kg/lbs)

### 📆 History View
- List of past entries with summaries.
- Filter data by date range.

### 📊 Data Visualization
- Track trends over time using bar/line charts powered by **MPAndroidChart**.
- View progress for the last 7 or 30 days.

### ⏰ Daily Reminders
- Receive a notification at a user-defined time to input health metrics.
- Built using **WorkManager**.

### ⚙️ Settings
- Toggle light/dark mode.
- Set reminder time.
- Export data to CSV or PDF.

---

## 🧱 Tech Stack

| Area               | Technology            |
|--------------------|------------------------|
| Language           | Kotlin                |
| UI                 | Jetpack Compose        |
| Architecture       | SOLID Principles, MVVM |
| Storage            | Firebase Firestore     |
| Authentication     | Firebase Auth          |
| Charts             | MPAndroidChart         |
| Background Tasks   | WorkManager            |

---

## 🔒 Authentication

- Firebase Authentication (Email/Password)
- User-specific data persisted securely via Firestore

---

## 📤 Data Export

- Export all health data to `.csv` or `.pdf` for backup or analysis.
- Share via email or cloud apps.

---

## 🌙 Dark Mode Support

- Fully responsive light and dark theme
- Toggle in settings

---

## 🚧 Setup & Installation

### Prerequisites
- Android Studio
- Firebase project (Firestore + Auth enabled)

### Clone and Run

```bash
https://github.com/aliabbasnagari/FitDiary
```
