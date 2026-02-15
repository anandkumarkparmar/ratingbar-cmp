plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    androidTarget()
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonSample"
            isStatic = false
        }
    }
    
    js(IR) {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":ratingbar-cmp"))
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.foundation)
                implementation(libs.compose.mpp.ui)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}

android {
    namespace = "com.github.anandkumarkparmar.ratingbar.sample.common"
    compileSdk = libs.versions.compileSdk.get().toInt()
    
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlin {
        jvmToolchain(17)
    }
}
