plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "RatingBarSample"
            isStatic = false
        }
    }
    
    val ratingbarVersion = project.findProperty("ratingbarVersion") as? String ?: "0.2.0"

    sourceSets {
        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation("com.github.anandkumarkparmar:ratingbar-cmp:$ratingbarVersion")
                implementation(project(":common"))
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.foundation)
                implementation(libs.compose.mpp.material3)
                implementation(libs.compose.mpp.ui)
            }
        }
        
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}
