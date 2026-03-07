# Roadmap

> Last updated: March 2026

---

## Current State (v0.1.0)

### Delivered

- State-hoisted `RatingBar` API (`value` / `onValueChange`)
- Fractional values and item fill fractions
- Touch, tap, drag, and keyboard interactions
- RTL behavior for visuals and interactions
- Read-only mode
- Slot API (`itemContent`) for custom rendering
- Internal default vectors (`RatingBarIcons`) without `material-icons-extended`
- Shared multiplatform sample app with Standard + Customization pages
- JitPack-ready publishing and CI validation pipeline
- Artifact size budgets and badge generation

### Platform Matrix

| Feature | Android | Desktop | iOS | Web |
|---|---|---|---|---|
| Core `RatingBar` composable | Yes | Yes | Yes | Yes |
| Tap + drag interaction | Yes | Yes | Yes | Yes |
| Keyboard interaction | Yes | Yes | Yes* | Yes |
| Fractional values | Yes | Yes | Yes | Yes |
| RTL support | Yes | Yes | Yes | Yes |
| Read-only mode | Yes | Yes | Yes | Yes |
| Slot API | Yes | Yes | Yes | Yes |

\* Keyboard behavior on iOS depends on the runtime input context.

---

## Possible Features and Enhancements

This is a comprehensive list of features, use cases, and improvements the library could support. Items are grouped by category, not by version or priority. Use this list to identify what to build next based on demand, feasibility, and impact.

---

### Interaction and Input

| Feature | Description |
|---|---|
| Hover / focus visual states | Visual feedback on hover (Desktop/Web) and focus ring customization |
| Long-press to reset | Reset rating to 0 on long-press gesture |
| Gesture sensitivity configuration | Tune drag sensitivity threshold for fine-grained control |
| Minimum value constraint | Enforce a minimum selectable value (e.g., must rate at least 1) |
| Clear / reset button support | Optional built-in clear button to zero out the rating |
| Double-tap to toggle | Double-tap to toggle between 0 and max |
| Allow-zero configuration | Explicit opt-in/out for allowing 0 as a valid value |

---

### Visual Customization

| Feature | Description |
|---|---|
| Fill animation | Animate fill transitions when value changes |
| Scale-on-select animation | Scale up the selected item momentarily |
| Shimmer / loading state | Display a shimmer placeholder while rating data loads |
| Color gradient fills | Apply gradient colors across the fill (e.g., red-to-green) |
| Shape presets | Built-in shapes: heart, circle, thumb-up, emoji, diamond |
| Custom icon packs | Easy integration of icon sets beyond the built-in stars |
| Outlined vs filled style toggle | Switch between outline-only and filled visual styles |
| Size animation on hover | Enlarge items on hover (Desktop/Web) |
| Glow / shadow effects | Apply glow or drop shadow to selected items |
| Theme-aware color presets | Material You dynamic color integration |
| Custom unfilled background | Support background patterns or colors for unfilled items |

---

### Accessibility

| Feature | Description |
|---|---|
| TalkBack / VoiceOver customization | Customize screen reader announcements per item |
| Custom semantic descriptions | Per-item semantic text (e.g., "Poor", "Average", "Excellent") |
| High-contrast mode support | Ensure visibility in high-contrast system themes |
| Reduced-motion support | Disable animations when the system prefers reduced motion |
| Focus ring customization | Customize focus indicator appearance |
| Accessibility actions | Support custom accessibility actions (e.g., "set to 3 stars") |

---

### Layout and Composition

| Feature | Description |
|---|---|
| Vertical orientation | Render items vertically instead of horizontally |
| Compact / inline mode | Single-line mode with label (e.g., "Rating: ★★★☆☆") |
| Badge mode | Compact star + numeric value display (e.g., "★ 4.5") |
| Label slot | Composable slot for a label (e.g., "4.5/5") alongside the bar |
| Responsive sizing | Auto-adjust item size based on available width |
| Grid layout | Multi-row layout for survey-style multi-question ratings |
| Leading / trailing content slots | Slots before/after the rating bar for icons or labels |

---

### Data and State

| Feature | Description |
|---|---|
| `rememberRatingBarState()` | Convenience function with built-in `remember` |
| Controlled vs uncontrolled mode | Support for uncontrolled usage with internal state |
| Form integration helpers | Helpers for common form libraries and patterns |
| Validation support | Required-field validation, min-value rules |
| State restoration / `Saver` | Automatic state persistence across configuration changes |
| `onValueChangeFinished` improvements | Debounce support, distinct-until-changed filtering |

---

### Advanced Features

| Feature | Description |
|---|---|
| Tooltip on hover | Show the current value as a tooltip on hover |
| Animated value transitions | Smooth animated transitions between rating values |
| Haptic feedback | Vibration feedback on value change (Android/iOS) |
| Sound feedback | Optional sound effect on interaction |
| Lottie / animated icon support | Use Lottie animations as rating items |
| Interaction source callback | Report whether the change came from touch, drag, keyboard, or programmatic API |
| Range rating | Support selecting a range (min-max) instead of a single value |
| Multi-criteria rating | Multiple rating dimensions in a single component |

---

### Platform-Specific Enhancements

| Feature | Platform | Description |
|---|---|---|
| Preview annotations | Android | `@Preview` support for Android Studio |
| UIKit interop wrapper | iOS | Wrapper for use in UIKit-based projects |
| ARIA attributes | Web | Enhanced ARIA roles and properties for web accessibility |
| Mouse wheel support | Desktop | Adjust rating with mouse scroll wheel |
| Stylus / S-Pen support | Android | Handle stylus input for precise control |
| Watch support | Android | Wear OS compatible variant |

---

### Distribution and Tooling

| Feature | Description |
|---|---|
| Maven Central publishing | Publish to Maven Central in addition to JitPack |
| Version catalog snippet | Provide ready-to-use Gradle version catalog entries |
| KMP module dependency docs | Document per-platform dependency setup |
| Kotlin/Wasm target | Add WebAssembly target support |
| BOM (Bill of Materials) | Provide a BOM for coordinated version management |
| API binary compatibility validation | Use `kotlinx.binary-compatibility-validator` |
| Dokka API docs | Auto-generated HTML documentation from KDoc |

---

### Testing and Quality

| Feature | Description |
|---|---|
| Compose UI tests | Automated Compose test rules for interaction scenarios |
| Screenshot tests | Visual regression tests for each platform |
| Fractional clipping tests | Deterministic tests for `FractionalClipShape` behavior |
| RTL interaction tests | Tests for RTL drag, tap, and keyboard behavior |
| Performance benchmarks | Measure and track recomposition performance |
| Accessibility audits | Automated accessibility checks in CI |

---

## Versioning Policy

- Semantic Versioning (`MAJOR.MINOR.PATCH`)
- `0.1.0` is the current baseline
- Breaking API changes bump the minor version during `0.x` development
- New features and improvements target the next minor release
