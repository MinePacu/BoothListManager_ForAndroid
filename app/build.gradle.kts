import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.chaquo.python")
}

android {
    namespace = "com.minepacu.boothlistmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.minepacu.boothlistmanager"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    flavorDimensions += "pyVersion"
    productFlavors {
        create("py310") { dimension = "pyVersion" }
        create("py311") { dimension = "pyVersion" }
    }

    viewBinding {
        enable = true
    }
}

chaquopy {
    productFlavors {
        getByName("py310") { version = "3.10" }
        getByName("py311") { version = "3.11" }
    }

    defaultConfig {
        pip {
            install("gspread")
            install("gspread-formatting")
            install("clipboard")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //implementation("com.google.android.gms:play-services-auth:21.0.0")
    //implementation("com.google.android.gms:play-services-drive:17.0.0")
    //implementation("com.google.api-client:google-api-client-android:1.23.0") {
    //    exclude(group = "org.apache.httpscomponents")
    //}
    //implementation("com.google.apis:google-api-services-sheets:v4-rev614-1.18.0-rc") {
    //    exclude(group = "org.apache.httpscomponents")
    //}
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.android.support:recyclerview-v7:28.0.0")
    //implementation("androidx.compose.material:material:1.6.3")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha05")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}