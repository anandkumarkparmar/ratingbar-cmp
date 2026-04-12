# Roadmap

This roadmap lists features and improvements under consideration for future releases of `ratingbar-cmp`. Items are grouped by theme, not priority or version.

**Being listed here is not a commitment to ship.** Priorities shift based on user demand, feasibility, maintainer bandwidth, and technical trade-offs discovered during implementation.

For what's already shipped, see [CHANGELOG.md](../CHANGELOG.md).

---

## Current State

- Current release: **0.5.0** — see the [0.5.0 changelog entry](../CHANGELOG.md#050---2026-04-11) for what's in the latest version.
- Versioning: [Semantic Versioning](https://semver.org/spec/v2.0.0.html). During the `0.x` series, breaking changes bump the minor version.
- The public API is tracked via the [Binary Compatibility Validator](https://github.com/Kotlin/binary-compatibility-validator) — any intentional API change requires regenerating the baseline.

---

## Interaction and Input

| Feature | Description |
|---|---|
| Gesture sensitivity configuration | Tunable drag threshold for fine-grained control |
| Clear / reset button | Optional built-in clear button to zero out the rating |
| Double-tap to toggle | Double-tap toggles between zero and max |

---

## Visual Customization

| Feature | Description |
|---|---|
| Custom icon packs | Easier integration of icon sets beyond the built-in options |
| Outlined vs filled style toggle | Single-flag switch between outline-only and filled visual styles |
| Size animation on hover | Enlarge items on hover (Desktop/Web) |
| Glow / shadow effects | Apply glow or drop shadow to selected items |
| Theme-aware color presets | Material You dynamic color integration |
| Custom unfilled background | Background patterns or colors for unfilled items |

---

## Accessibility

| Feature | Description |
|---|---|
| High-contrast mode support | Ensure visibility in high-contrast system themes |
| Focus ring customization | Customize the keyboard-focus indicator appearance |

---

## Layout and Composition

| Feature | Description |
|---|---|
| Vertical orientation | Render items vertically instead of horizontally |
| Compact / inline mode | Single-line mode with label (e.g., "Rating: ★★★☆☆") |
| Badge mode | Compact star + numeric value display (e.g., "★ 4.5") |
| Responsive sizing | Auto-adjust item size based on available width |
| Grid layout | Multi-row layout for survey-style multi-question ratings |

---

## Data and State

| Feature | Description |
|---|---|
| Controlled vs uncontrolled mode | Support for uncontrolled usage with internal state |
| Form integration helpers | Helpers for common form libraries and patterns |
| Validation support | Required-field validation and min-value rules |
| `onValueChangeFinished` improvements | Debounce support and distinct-until-changed filtering |

---

## Advanced Features

| Feature | Description |
|---|---|
| Tooltip on hover | Show the current value as a tooltip on hover |
| Sound feedback | Optional sound effect on interaction |
| Lottie / animated icon support | Use Lottie animations as rating items |
| Range rating | Select a range (min–max) instead of a single value |
| Multi-criteria rating | Multiple rating dimensions in a single component |

---

## Platform-Specific Enhancements

| Feature | Platform | Description |
|---|---|---|
| Preview annotations | Android | `@Preview` support for Android Studio |
| UIKit interop wrapper | iOS | Wrapper for use in UIKit-based (non-Compose) projects |
| Enhanced ARIA attributes | Web | Richer ARIA roles and properties |
| Stylus / S-Pen support | Android | Handle stylus input for precise control |
| Wear OS variant | Android | Wear OS compatible rating bar |

---

## Distribution and Tooling

| Feature | Description |
|---|---|
| Maven Central publishing | Publish to Maven Central alongside JitPack |
| Custom documentation site | MkDocs Material theme over `/docs/` — searchable, themed portal on GitHub Pages |
| Version catalog snippet | Ready-to-use Gradle version catalog entries |
| KMP module dependency docs | Per-platform dependency setup documentation |
| Kotlin/Wasm target | Add WebAssembly target support |
| Bill of Materials (BOM) | Coordinated version management across modules |

---

## Testing and Quality

| Feature | Description |
|---|---|
| Screenshot tests | Visual regression tests for each platform |
| RTL interaction tests | Tests for RTL drag, tap, and keyboard behavior |
| Performance benchmarks | Measure and track recomposition performance |
| Accessibility audits | Automated accessibility checks in CI |

---

## Proposing New Items?

Found something missing? Have a use case the library doesn't cover?

- Open a [GitHub Discussion](https://github.com/anandkumarkparmar/ratingbar-cmp/discussions) to talk through the idea first — best for early-stage or exploratory proposals.
- Or file a [Feature Request issue](https://github.com/anandkumarkparmar/ratingbar-cmp/issues/new?template=feature_request.md) if the shape of the feature is already clear.

See [CONTRIBUTING.md](../CONTRIBUTING.md) for the full contribution workflow once you're ready to implement something.
