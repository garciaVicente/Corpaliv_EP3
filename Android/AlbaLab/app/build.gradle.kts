plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

android {
    namespace = "com.example.corpaliv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.corpaliv"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "3.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Android y Jetpack Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Animaciones Compose con versiones alineadas al BOM
    implementation("androidx.compose.animation:animation")
    constraints {
        implementation("androidx.compose.animation:animation") {
            version { strictly("1.9.1") }
        }
        implementation("androidx.compose.animation:animation-core") {
            version { strictly("1.9.1") }
        }
        implementation("androidx.compose.runtime:runtime-saveable") {
            version { strictly("1.9.1") }
        }
    }

    // Room: persistencia local y caché
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ViewModel y ciclo de vida
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")

    // Retrofit y OkHttp: conexión con el API Gateway
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Pruebas y herramientas
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling)
}
