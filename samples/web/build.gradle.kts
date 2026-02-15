plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":ratingbar-cmp"))
                implementation(project(":samples:common"))
                implementation(libs.compose.mpp.html.core)
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}
