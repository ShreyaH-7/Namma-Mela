# Namma Mela

Namma Mela is a full-stack theatre experience app with:
- Android frontend in Kotlin using MVVM, Navigation Component, ViewBinding, Material Design, Retrofit, and Room.
- Node.js backend using Express, JWT authentication, and MongoDB.

## Project Structure
- [android-app](C:/Users/SHREYA/Documents/Codex/2026-04-27/act-as-a-senior-android-backend/android-app)
- [backend](C:/Users/SHREYA/Documents/Codex/2026-04-27/act-as-a-senior-android-backend/backend)

## Features
- User registration, login, session persistence, and logout
- Home screen showing today's play details from backend
- Seat booking with available, selected, and reserved states
- Fan wall with comment posting and list refresh
- Admin login with PIN or credentials
- Admin tools for managing play details, cast, and seat reset
- Room-based local caching for play, bookings, comments, and cast
- Light and dark theme support based on system setting

## Backend Setup
1. Go to [backend](C:/Users/SHREYA/Documents/Codex/2026-04-27/act-as-a-senior-android-backend/backend)
2. Copy `.env.example` to `.env`
3. Install packages with `npm install`
4. Start MongoDB locally or point `MONGODB_URI` to your MongoDB instance
5. Run `npm run seed`
6. Run `npm run dev`

Default admin seed values:
- Email: `admin@nammamela.com`
- Password: `Admin@123`
- PIN: `4321`

## Android Setup
1. Open [android-app](C:/Users/SHREYA/Documents/Codex/2026-04-27/act-as-a-senior-android-backend/android-app) in Android Studio
2. Let Gradle sync dependencies
3. Start the backend on port `5000`
4. Run the app on the Android emulator

The Android app uses `http://10.0.2.2:5000/api/` as the default local API base URL for emulator access.

## Database Design
See [DATABASE_SCHEMA.md](C:/Users/SHREYA/Documents/Codex/2026-04-27/act-as-a-senior-android-backend/DATABASE_SCHEMA.md).
<img width="601" height="677" alt="image" src="https://github.com/user-attachments/assets/9272d632-3f26-4b1c-bd1d-7a71414ff16a" />

<img width="476" height="681" alt="image" src="https://github.com/user-attachments/assets/2dceab73-3430-4bcd-82b0-5cfa475b0abb" />

<img width="491" height="765" alt="image" src="https://github.com/user-attachments/assets/6040b7bd-120d-4d40-be7b-eb640e3dcb5e" />

<img width="467" height="559" alt="image" src="https://github.com/user-attachments/assets/9ddfcbaa-4fc1-4098-b28f-27e0a882def7" />

<img width="484" height="451" alt="image" src="https://github.com/user-attachments/assets/9ff95262-cc8e-4899-9c9d-aa747beaab33" />

<img width="547" height="549" alt="image" src="https://github.com/user-attachments/assets/bfce4aba-8733-496e-8709-82fca78a6cae" />

<img width="528" height="702" alt="image" src="https://github.com/user-attachments/assets/8ea7df1a-e4fc-447c-b6d6-775ed676428a" />





