rootProject.name = "ratingbar-cmp"

include(":ratingbar-cmp")
include(":samples:common")
include(":samples:android")
include(":samples:desktop")
include(":samples:ios")
include(":samples:web")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

val useLocalLibrary = providers.gradleProperty("useLocalLibrary").orElse("true").get().toBoolean()

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        if (!useLocalLibrary) {
            maven("https://jitpack.io")
        }
    }
}
