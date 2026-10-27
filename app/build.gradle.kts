plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.arrax"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.arrax"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(
            org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        )
    }
}

dependencies {
    // ─────────────────────────────────────
    // Android Core
    // ─────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ─────────────────────────────────────
    // Jetpack Compose
    // ─────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)

    // ─────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────
    implementation(libs.androidx.navigation.compose)

    // ─────────────────────────────────────
    // Dependency Injection - Hilt
    // ─────────────────────────────────────
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // ─────────────────────────────────────
    // Firebase
    // ─────────────────────────────────────
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)

    // ─────────────────────────────────────
    // Data Storage
    // ─────────────────────────────────────
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    // ─────────────────────────────────────
    // Image Loading - Coil 3
    // ─────────────────────────────────────
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // ─────────────────────────────────────
    // Authentication - Credential Manager
    // ─────────────────────────────────────
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.id)

    // ─────────────────────────────────────
    // QR Generation - ZXing
    // ─────────────────────────────────────
    implementation(libs.zxing.core)

    // ─────────────────────────────────────
    // QR Scanning - CameraX
    // ─────────────────────────────────────
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.google.guava)
    // ─────────────────────────────────────
    // Barcode & QR Recognition - ML Kit
    // ─────────────────────────────────────
    implementation(libs.google.mlkit.barcode.scanning)

    // ─────────────────────────────────────
    // Unit Tests
    // ─────────────────────────────────────
    testImplementation(libs.junit)

    // ─────────────────────────────────────
    // Instrumented Tests
    // ─────────────────────────────────────
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ─────────────────────────────────────
    // Debug Tools
    // ─────────────────────────────────────
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
