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
    
    val useLocalLibrary = (project.findProperty("useLocalLibrary") as? String)?.toBoolean() ?: true
    val ratingbarVersion = project.findProperty("ratingbarVersion") as? String ?: "0.2.0"

    sourceSets {
        val jsMain by getting {
            dependencies {
                if (useLocalLibrary) {
                    implementation(project(":ratingbar-cmp"))
                } else {
                    implementation("com.github.anandkumarkparmar:ratingbar-cmp:$ratingbarVersion")
                }
                implementation(project(":samples:common"))
                implementation(libs.compose.mpp.html.core)
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}
