plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "org.n9ne.common"
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

kapt {
    correctErrorTypes = true
}
dependencies {

    implementation("com.github.yagmurerdogan:Toastic:1.0.1")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation("com.github.YarikSOffice:lingver:1.3.0")

    implementation("com.github.kojofosu:Quantitizer:1.6.8")

    implementation("com.guolindev.permissionx:permissionx:1.7.1")

    implementation("com.github.samanzamani:PersianDate:1.7.1")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation("com.github.majorkik:SparkLineLayout:1.0.1")
    implementation("com.github.BoyzDroizy:SimpleAndroidBarChart:1.0.1")

    implementation("com.github.SimformSolutionsPvtLtd:SSPullToRefresh:1.5.1")

    implementation("com.airbnb.android:lottie:6.1.0")

    implementation("com.patrykandpatrick.vico:core:1.12.0")
    implementation("com.patrykandpatrick.vico:views:1.12.0")

    testImplementation("junit:junit:4.12")

    implementation("com.github.Dhaval2404:ColorPicker:2.3")

    implementation("com.github.aliab:Persian-Date-Picker-Dialog:1.8.0")

    implementation("me.saket.cascade:cascade:2.3.0")
    implementation("me.saket.cascade:cascade-compose:2.3.0")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    val roomVersion = "2.5.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-base:18.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}