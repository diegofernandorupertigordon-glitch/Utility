plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "app.application.utility"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.application.utility"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    // 🧱 Core Compose
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.compose.ui:ui:1.9.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.9.0")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.animation:animation:1.9.0")

    // ✅ Icons Extended estable
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // 🧭 Navegación
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 🧠 Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // 🎨 BOM Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.graphics)

    // 🧪 Testing / Debug
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.0")
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // 🔥🔥🔥 FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    dependencies {
        implementation("io.coil-kt:coil-compose:2.5.0")
        implementation("io.coil-kt:coil-compose:2.6.0")

    }
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

}
