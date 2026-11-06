import java.net.URI

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "org.n9ne.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "API_BASE_URL", "\"https://kaizen-team.ir/h2oHealthy/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            val localBackendUrl = providers.gradleProperty("localBackendUrl").orNull
            if (!localBackendUrl.isNullOrBlank()) {
                val uri = URI(localBackendUrl)
                require(
                    uri.scheme in listOf(
                        "http",
                        "https"
                    ) && uri.host != null && localBackendUrl.endsWith('/')
                ) {
                    "localBackendUrl must be an HTTP(S) URL ending in /"
                }
                require(uri.userInfo == null && uri.query == null && uri.fragment == null)
                buildConfigField("String", "API_BASE_URL", "\"$localBackendUrl\"")
            }
        }
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
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

kapt {
    correctErrorTypes = true
}
dependencies {

    implementation(libs.toastic)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.lingver)

    implementation(libs.quantitizer)

    implementation(libs.permissionx)

    implementation(libs.persian.date)

    implementation(libs.play.services.auth)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.sparkline)
    implementation(libs.simpleandroidbarchart)

    implementation(libs.pull.to.refresh)

    implementation(libs.lottie)

    implementation(libs.vico.core)
    implementation(libs.vico.views)


    implementation(libs.color.picker)

    implementation(libs.persian.date.picker)

    implementation(libs.cascade.views)
    implementation(libs.cascade.compose)

    implementation(libs.glide.runtime)
    kapt(libs.glide.compiler)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.components)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.base)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
