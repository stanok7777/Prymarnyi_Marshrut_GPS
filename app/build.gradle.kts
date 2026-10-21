plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.ghostroute.stealthnav"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.ghostroute.stealthnav"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
dependencies {
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    // Бібліотека для P2P з'єднання без інтернету
    implementation("com.google.android.gms:play-services-nearby:18.7.0")
}