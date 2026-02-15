# ratingbar-cmp Roadmap

> Last updated: 14 February 2026

A lightweight, accessible, Compose Multiplatform rating component for Android, Desktop, iOS, and Web.

---

## Current Status

### Release State

- **Current stable baseline**: `0.1.0`
- **Feature set frozen** for `0.1.0`
- **Distribution**: Via JitPack (`com.github.anandkumarkparmar:ratingbar-cmp:0.1.0`)

### Architecture

- `ratingbar-cmp` — library module (core logic + composable UI)
- `samples:common` — shared KMP sample UI
- `samples:android` / `samples:desktop` / `samples:ios` / `samples:web` — platform launchers

---

## v0.1.0

### Delivered

- ✅ State-hoisted `RatingBar` API
- ✅ Fractional values and item fill fractions
- ✅ Touch/tap/drag/keyboard interactions
- ✅ RTL behavior for visuals and interactions
- ✅ Read-only mode
- ✅ Slot API (`itemContent`) for custom rendering
- ✅ Internal default vectors (`RatingBarIcons`) without `material-icons-extended`
- ✅ Shared sample app with Standard + Customization pages
- ✅ Android edge-to-edge sample support
- ✅ JitPack-ready publish and CI validation pipeline

### Platform Feature Matrix

| Feature | Android | Desktop | iOS | Web |
|---|---|---|---|---|
| Core `RatingBar` composable | ✅ | ✅ | ✅ | ✅ |
| Tap + drag interaction | ✅ | ✅ | ✅ | ✅ |
| Keyboard interaction | ✅ | ✅ | ✅* | ✅ |
| Fractional values | ✅ | ✅ | ✅ | ✅ |
| RTL support | ✅ | ✅ | ✅ | ✅ |
| Read-only mode | ✅ | ✅ | ✅ | ✅ |
| Slot API | ✅ | ✅ | ✅ | ✅ |
| Shared sample UI | ✅ | ✅ | ✅ | ✅ |

\* Keyboard behavior depends on runtime/input context for platform shell.

---

## Planned (Next)

- [ ] Add screenshot gallery for each platform in README
- [ ] Add Compose UI tests for sample scenarios (Android/Desktop)
- [ ] Add deterministic tests for fractional clipping behavior
- [ ] Add deterministic tests for RTL drag + keyboard behavior
- [ ] Add callback for interaction source (`touch`, `drag`, `keyboard`)
- [ ] Add optional hover/focus state customization API
- [ ] Add `RatingBarDefaults` color presets for common themes
- [ ] Add compact “badge + rating” sample patterns
- [ ] Add migration guide and “known integration pitfalls” section
- [ ] Add adopters section populated from community shoutouts

---

## Community Trust Strategy

- Encourage real-world adopters to submit shoutouts via GitHub issue
- Maintain an adopters section with verified app/project references
- Highlight usage patterns and reliability signals for new users

---

## Versioning Policy

- Semantic Versioning (`MAJOR.MINOR.PATCH`)
- `0.1.0` is the current frozen baseline
- New features move to next minor line after baseline stabilization
