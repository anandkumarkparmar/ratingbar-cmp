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

    sourceSets {
        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(project(":samples:common"))
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
