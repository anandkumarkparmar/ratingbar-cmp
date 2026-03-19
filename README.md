# ratingbar-cmp

![HeroImage](assets/readme-hero-image.svg)

[![JitPack](https://jitpack.io/v/anandkumarkparmar/ratingbar-cmp.svg)](https://jitpack.io/#anandkumarkparmar/ratingbar-cmp)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.10-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.1-blue)](https://github.com/JetBrains/compose-multiplatform)

[![Android AAR size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/android-aar-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Desktop published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/desktop-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![iOS published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/ios-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Web published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/web-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Total published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/total-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)

A lightweight, accessible, Compose Multiplatform RatingBar component for Android, Desktop, iOS, and Web.

---

## Installation

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.anandkumarkparmar:ratingbar-cmp:0.2.0")
}
```

---

## Basic Usage

```kotlin
import androidx.compose.runtime.*
import com.github.anandkumarkparmar.ratingbar.RatingBar

@Composable
fun RatingExample() {
    var rating by remember { mutableStateOf(3.5f) }

    RatingBar(
        value = rating,
        onValueChange = { rating = it },
        max = 5,
        step = 0.5f
    )
}
```

---

## Platform Coverage

| Capability | Android | Desktop | iOS | Web |
|---|---|---|---|---|
| Interactive rating | Yes | Yes | Yes | Yes |
| Fractional steps | Yes | Yes | Yes | Yes |
| RTL behavior | Yes | Yes | Yes | Yes |
| Read-only mode | Yes | Yes | Yes | Yes |
| Custom slot API | Yes | Yes | Yes | Yes |
| Shared sample UI | Yes | Yes | Yes | Yes |

---

## Documentation

- [API Reference](docs/api-reference.md)
- [Running Samples](docs/running-samples.md)
- [Contributing](docs/contributing.md)
- [Roadmap](docs/roadmap.md)
- [Changelog](docs/changelog.md)
- [Publishing Checklist](docs/publishing-checklist.md)
- [CI/CD Guide](docs/ci-guide.md)
- [Code of Conduct](docs/code-of-conduct.md)

---

## Community Shoutout

Using `ratingbar-cmp` in your app? Open an issue titled **"Shoutout: \<Your App Name\>"** with your app name, platform(s), a short use case, and an optional link or screenshot.

---

## License

Apache-2.0. See [LICENSE](LICENSE).
