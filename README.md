<div align="center">

![HeroImage](assets/readme-hero-image.svg)

[![JitPack](https://jitpack.io/v/anandkumarkparmar/ratingbar-cmp.svg)](https://jitpack.io/#anandkumarkparmar/ratingbar-cmp)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.1-FF6D00)](https://github.com/JetBrains/compose-multiplatform)
[![API Docs](https://img.shields.io/badge/API%20Docs-Website-0075CA)](https://anandkumarkparmar.github.io/ratingbar-cmp/)
[![Try It Live](https://img.shields.io/badge/Try%20It%20Live-Demo-blueviolet)](https://anandkumarkparmar.github.io/ratingbar-cmp/demo/)

[![Android AAR size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/android-aar-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Desktop published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/desktop-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![iOS published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/ios-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Web published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/web-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)
[![Total published size](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/anandkumarkparmar/ratingbar-cmp/main/.github/badges/total-published-size.json)](https://github.com/anandkumarkparmar/ratingbar-cmp/actions)

A lightweight, accessible, Compose Multiplatform RatingBar component for Android, Desktop, iOS, and Web.

</div>

---

## See It in Action

<table align="center">
  <tr>
    <td align="center" width="100"><b>Android</b></td>
    <td align="center"><img src="assets/demos/demo-android.gif" width="200"/></td>
  </tr>
  <tr>
    <td align="center"><b>Desktop</b></td>
    <td align="center"><img src="assets/demos/demo-desktop.gif" width="500"/></td>
  </tr>
</table>

---

## Why ratingbar-cmp?

- **Zero extra dependencies** — only Compose Multiplatform, nothing else
- **Fractional steps** — 0.5, 0.25, or any custom step size
- **Fully accessible** — keyboard navigation, RTL layout, screen reader semantics on all platforms
- **Slot API** — supply any composable as a rating item (hearts, circles, custom icons)
- **Platform-native feel** — hover preview on Desktop/Web, haptic feedback on Android, mouse wheel support
- **Gradient fills** — apply a color gradient brush via `RatingBarColors.fillBrush`
- **Shimmer placeholder** — `RatingBarPlaceholder` renders an animated skeleton while data loads
- **Long-press to reset** — opt-in long-press resets to effective minimum via `RatingBarBehavior`
- **Shape variety** — built-in Heart, ThumbUp, and Circle icon pairs alongside the default stars

---

## Installation

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:0.4.0")
}
```

---

## Basic Usage

```kotlin
import androidx.compose.runtime.*
import com.github.anandkumarkparmar.ratingbar.*
import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig

@Composable
fun RatingExample() {
    var rating by rememberRatingBarState(initialValue = 3f)

    RatingBar(
        value = rating,
        onValueChange = { rating = it },
        config = RatingBarConfig(max = 5, step = 0.5f),
        style = RatingBarDefaults.style(itemSize = RatingBarDefaults.SizeMedium),
        animations = RatingBarDefaults.animations(enabled = true),
        behavior = RatingBarDefaults.behavior(hapticFeedback = true),
    )
}
```

For all parameters, overloads, and advanced usage see the [API Reference](docs/api-reference.md) or browse the [live API docs](https://anandkumarkparmar.github.io/ratingbar-cmp/).

> **Migrating from v0.3.0?** Individual parameters like `max`, `step`, `filledColor`, and
> `animateRating` are now grouped into `config`, `style`, `animations`, and `behavior` objects.
> Call sites using all defaults need no changes. See the
> [migration guide](docs/api-reference.md#migrating-from-v030) and
> [changelog](docs/changelog.md) for details.

---

## Platform Coverage

| Feature | Android | Desktop | iOS | Web |
|---|---|---|---|---|
| Core `RatingBar` composable | ✓ | ✓ | ✓ | ✓ |
| Tap + drag interaction | ✓ | ✓ | ✓ | ✓ |
| Keyboard interaction | ✓ | ✓ | ✓* | ✓ |
| Fractional values | ✓ | ✓ | ✓ | ✓ |
| RTL support | ✓ | ✓ | ✓ | ✓ |
| Read-only mode | ✓ | ✓ | ✓ | ✓ |
| Slot API | ✓ | ✓ | ✓ | ✓ |
| Fill animation | ✓ | ✓ | ✓ | ✓ |
| Scale-on-select animation | ✓ | ✓ | ✓ | ✓ |
| Minimum value constraint | ✓ | ✓ | ✓ | ✓ |
| Hover preview | — | ✓ | — | ✓ |
| Mouse wheel scroll | — | ✓ | — | — |
| Haptic feedback | ✓ | — | — | — |
| Long-press to reset | ✓ | ✓ | ✓ | ✓ |
| Shimmer placeholder | ✓ | ✓ | ✓ | ✓ |

\* Keyboard behavior on iOS depends on the runtime input context.<br>
— Feature is a graceful no-op on this platform — no crash, no error.

---

## What's Coming

A glimpse of what's planned for future releases. See the [full roadmap](docs/roadmap.md) for details.

| Feature | Status |
|---|---|
| Gesture sensitivity configuration | Planned |
| Compact / inline mode | Planned |
| Badge mode (★ 4.5) | Planned |
| Kotlin/Wasm target | Planned |

See the [full roadmap](docs/roadmap.md) for everything delivered and everything still planned.

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
