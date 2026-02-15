plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":ratingbar-cmp"))
                implementation(project(":samples:common"))
                implementation(compose.desktop.currentOs)
                implementation(libs.compose.mpp.material3)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.github.anandkumarkparmar.ratingbar.sample.desktop.MainKt"
        
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "RatingBar Sample"
            packageVersion = "1.0.0"
        }
    }
}
