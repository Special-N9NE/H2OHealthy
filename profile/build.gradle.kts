plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "org.n9ne.profile"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    api("com.github.bumptech.glide:glide:4.15.1")

    api("com.github.aliab:Persian-Date-Picker-Dialog:1.8.0")

    api("me.saket.cascade:cascade:2.3.0")
    api("me.saket.cascade:cascade-compose:2.3.0")

    api("androidx.navigation:navigation-fragment-ktx:2.7.4")
    api("androidx.navigation:navigation-ui-ktx:2.7.4")


    val roomVersion = "2.5.2"
    api("androidx.room:room-ktx:$roomVersion")
    api("androidx.room:room-runtime:$roomVersion")

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.9.0")

    api("androidx.core:core-ktx:1.12.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.10.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(":common"))
}