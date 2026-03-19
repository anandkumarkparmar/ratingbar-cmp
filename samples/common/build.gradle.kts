plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.github.anandkumarkparmar.ratingbar.sample.common"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
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
    
    val ratingbarVersion = project.findProperty("ratingbarVersion") as? String ?: "0.3.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.anandkumarkparmar:ratingbar-cmp:$ratingbarVersion")
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.foundation)
                implementation(libs.compose.mpp.ui)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}
