plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "org.n9ne.auth"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

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

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    api(libs.play.services.auth)

    api(libs.persian.date.picker)

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
    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)

    implementation(project(":common"))
}