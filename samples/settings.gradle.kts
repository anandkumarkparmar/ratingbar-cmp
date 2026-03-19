rootProject.name = "ratingbar-cmp-samples"

// Composite build: substitute the published library coordinate with the local library project.
// Gradle will use the local library automatically during development/CI.
includeBuild("..") {
    dependencySubstitution {
        substitute(module("com.github.anandkumarkparmar:ratingbar-cmp")).using(project(":"))
    }
}

include(":common")
include(":android")
include(":desktop")
include(":ios")
include(":web")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    // Share the library's version catalog — single source of truth for all versions.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}
