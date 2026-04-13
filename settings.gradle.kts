rootProject.name = "ratingbar-cmp-parent"

// Pass -PuseRemote -PremoteLibraryVersion=1.0.0 to resolve the library from
// JitPack instead of the local project. Useful for smoke-testing a published
// release end-to-end through the sample apps.
val useRemote = providers.gradleProperty("useRemote").isPresent

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
        if (useRemote) {
            maven("https://jitpack.io")
        }
    }
}

// Library module — excluded when testing against a remote (JitPack) artifact.
if (!useRemote) {
    include(":ratingbar-cmp")
}

// Sample modules (flattened from former composite build)
include(":samples:common")
include(":samples:android")
include(":samples:desktop")
include(":samples:ios")
include(":samples:web")
