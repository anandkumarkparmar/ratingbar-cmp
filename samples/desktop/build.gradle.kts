plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm("desktop")
    
    val ratingbarVersion = project.findProperty("ratingbarVersion") as? String ?: "0.2.0"

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation("com.github.anandkumarkparmar:ratingbar-cmp:$ratingbarVersion")
                implementation(project(":common"))
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
