plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kps)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.daggerHiltAndroid)



}

android {
    namespace = "com.example.media"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.media"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "dagger.fastInit" to "enabled",
                        "dagger.hilt.android.internal.disableAndroidSuperclassValidation" to "true",
                        "dagger.hilt.android.internal.projectType" to "ANDROID",
                        "dagger.hilt.internal.useAggregatingRootProcessor" to "true"
                    )
                )
            }
        }
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

ksp {
    arg("moshiGenerateFactories", "true")
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
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Icon
    implementation(libs.material.icons)

    //navigation
    implementation (libs.androidx.navigation.compose)

    // Serialization Json - Safe args
    implementation(libs.kotlinx.serialization.json)

    // Coil
    implementation(libs.coil)

    // Dagger Hilt Android

    implementation(libs.dagger.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.hilt.navigation)



    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)


    // Glide
    implementation(libs.glide.compose)
    implementation(libs.accompanist.glide)



}