plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")


}

android {
    namespace = "com.ojijo.o_invest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ojijo.o_invest"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.material:material-icons-extended:1.6.1")

    implementation ("androidx.compose.material3:material3:1.1.2")
    implementation ("androidx.compose.material3:material3:1.2.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.10.1")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")



    //Face scan
    implementation ("androidx.camera:camera-core:1.3.0")
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("com.google.mlkit:face-detection:16.1.5")



    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Accompanist Pager Library (latest stable version)
    implementation("com.google.accompanist:accompanist-pager:0.24.13-rc") // Ensure correct version
    implementation("androidx.compose.foundation:foundation:1.5.0")  // Latest stable

    implementation ("com.google.android.exoplayer:exoplayer:2.17.1") //For videosplashscreen







    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
}



