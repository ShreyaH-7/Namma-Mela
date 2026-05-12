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
