plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.24" apply false

    // ✅ ADD THIS (this is your missing one)
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}