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
        versionName = "1.0.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("com.google.android.gms:play-services-nearby:18.7.0")
}