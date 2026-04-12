// Root aggregator build file.
//
// Responsibilities:
//   1. Declaring shared plugins with `apply false` so subprojects can apply them
//      consistently without re-resolving versions or loading the Kotlin plugin
//      multiple times across subproject classloaders.
//   2. Applying cross-cutting quality tooling (detekt) to every subproject with
//      a single shared configuration (`detekt.yml` at repo root).
//
// No source code, no dependencies, no publishing config lives here — those are
// all scoped to the `:ratingbar-cmp` library subproject.

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
}

// Apply detekt uniformly to every subproject that has Kotlin source.
// Shared config lives at the repo root so library and samples use identical rules.
//
// For KMP modules, detekt's default `detekt` task looks for a `main` source set
// (Java convention) and silently reports NO-SOURCE. We explicitly list the
// conventional KMP and Android source directories so the aggregate task lints
// every Kotlin file in every module. Non-existent directories are ignored.
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        config.setFrom(rootProject.file("detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
        source.setFrom(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
            "src/desktopMain/kotlin",
            "src/iosMain/kotlin",
            "src/jsMain/kotlin",
            "src/main/kotlin",
            "src/main/java"
        )
    }

    tasks.withType<Detekt>().configureEach {
        // Keep reports consistent across modules.
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}
