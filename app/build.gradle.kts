plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "org.n9ne.h2ohealthy"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.n9ne.h2ohealthy"
        minSdk = 26
        targetSdk = 34
        versionCode = 13
        versionName = "4.0.0"

        resourceConfigurations.addAll(mutableListOf("en" , "fa"))

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
        viewBinding = true
        dataBinding = true
    }
}

kapt {
    correctErrorTypes = true
}
dependencies {
    implementation(libs.appmetrica.analytics)


    api(libs.hilt.android)
    kapt(libs.hilt.compiler)

    api(libs.lingver)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)

    api(libs.sparkline)
    api(libs.simpleandroidbarchart)

    api(libs.pull.to.refresh)

    api(libs.lottie)

    api(libs.vico.core)
    api(libs.vico.views)


    api(libs.color.picker)

    api(libs.persian.date.picker)

    api(libs.cascade.views)
    api(libs.cascade.compose)

    api(libs.glide.runtime)

    api(libs.androidx.room.ktx)
    api(libs.androidx.room.runtime)

    api(libs.retrofit.core)
    api(libs.retrofit.converter.gson)
    api(libs.okhttp.logging.interceptor)

    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material.components)
    api(libs.androidx.constraintlayout)
    api(libs.play.services.base)
    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)

    implementation(project(":common"))
    implementation(project(":home"))
    implementation(project(":profile"))
    implementation(project(":auth"))
}
