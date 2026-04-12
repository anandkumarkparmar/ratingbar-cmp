plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.binary.compatibility.validator)
    id("maven-publish")
}

// Detekt is applied uniformly to every subproject by the root build.gradle.kts
// via `subprojects { apply(plugin = "io.gitlab.arturbosch.detekt") ... }` with
// shared config at the repo root (`detekt.yml`). No per-module configuration here.

group = "com.github.anandkumarkparmar"
version = property("libraryVersion") as String

kotlin {
    explicitApi()

    android {
        namespace = "com.github.anandkumarkparmar.ratingbar"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    js(IR) {
        browser()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "RatingBarCompose"
            isStatic = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.mpp.runtime)
                implementation(libs.compose.mpp.foundation)
                implementation(libs.compose.mpp.ui)
                implementation(libs.compose.mpp.material3)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
            }
        }

        val desktopMain by getting {
            dependencies {}
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

publishing {
    publications {
        configureEach {
            if (this is MavenPublication) {
                pom {
                    name.set("RatingBar CMP")
                    description.set("A lightweight, accessible, Compose Multiplatform RatingBar component for Android, Desktop, iOS, and Web.")
                    url.set("https://github.com/anandkumarkparmar/ratingbar-cmp")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("anandkumarkparmar")
                            name.set("Anand K Parmar")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/anandkumarkparmar/ratingbar-cmp.git")
                        developerConnection.set("scm:git:ssh://github.com/anandkumarkparmar/ratingbar-cmp.git")
                        url.set("https://github.com/anandkumarkparmar/ratingbar-cmp")
                    }
                }
            }
        }
    }
}
