plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "org.n9ne.h2ohealthy"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.n9ne.h2ohealthy"
        minSdk = 26
        targetSdk = 34
        versionCode = 5
        versionName = "2.1"

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
        dataBinding = true
    }
}

dependencies {

    api(platform("com.google.firebase:firebase-bom:32.7.0"))
    api("com.google.firebase:firebase-analytics")
    api("com.google.firebase:firebase-crashlytics")

    api("com.github.majorkik:SparkLineLayout:1.0.1")
    api("com.github.BoyzDroizy:SimpleAndroidBarChart:1.0.1")

    api("com.github.SimformSolutionsPvtLtd:SSPullToRefresh:1.5.1")

    api("com.airbnb.android:lottie:6.1.0")

    api("com.patrykandpatrick.vico:core:1.12.0")
    api("com.patrykandpatrick.vico:views:1.12.0")

    testApi("junit:junit:4.12")

    api("com.github.Dhaval2404:ColorPicker:2.3")

    api("com.github.aliab:Persian-Date-Picker-Dialog:1.8.0")

    api("me.saket.cascade:cascade:2.3.0")
    api("me.saket.cascade:cascade-compose:2.3.0")

    api("com.github.bumptech.glide:glide:4.15.1")

    val roomVersion = "2.5.2"
    api("androidx.room:room-ktx:$roomVersion")
    api("androidx.room:room-runtime:$roomVersion")

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.9.0")

    api("androidx.navigation:navigation-fragment-ktx:2.7.4")
    api("androidx.navigation:navigation-ui-ktx:2.7.4")

    api("androidx.core:core-ktx:1.12.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.10.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("com.google.android.gms:play-services-base:18.2.0")
    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(":common"))
    implementation(project(":home"))
    implementation(project(":profile"))
    implementation(project(":auth"))
}