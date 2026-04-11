rootProject.name = "ratingbar-cmp-parent"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

// Library module
include(":ratingbar-cmp")

// Sample modules (flattened from former composite build)
include(":samples:common")
include(":samples:android")
include(":samples:desktop")
include(":samples:ios")
include(":samples:web")
