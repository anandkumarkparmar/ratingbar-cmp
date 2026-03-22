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
    
    val ratingbarVersion = project.findProperty("ratingbarVersion") as? String ?: "0.3.0"

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:$ratingbarVersion")
                implementation(project(":common"))
                implementation(libs.compose.mpp.html.core)
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}
