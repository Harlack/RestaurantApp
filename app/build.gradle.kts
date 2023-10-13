 plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.restaurantapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.restaurantapp"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.4")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Dodatkowe jednostki miary do tekstu
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")
    // Gif do animacji ładowania
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
    // Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    //videoModel mvvm
    var version = "2.4.0-rc01"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$version")
    implementation ("android.arch.lifecycle:extensions:1.1.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}