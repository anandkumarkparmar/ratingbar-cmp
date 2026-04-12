# Contributing to ratingbar-cmp

Thanks for your interest in contributing to **ratingbar-cmp**. Whether you're fixing a typo, filing a bug, or shipping a new feature, this guide walks you through the whole process — including the first-contribution steps if this is new to you.

---

## Prerequisites

At a minimum, you'll need:

- **JDK 17** (Temurin recommended)
- **Android SDK** (compileSdk 36, minSdk 24) — only if you're working on Android
- **Xcode** (latest stable) — only if you're working on iOS; macOS only
- **Git**

Kotlin and Gradle are managed by the Gradle wrapper in the repo — no separate install needed.

**If you haven't set up the project yet, follow [SETUP.md](SETUP.md) first.** It has install pointers for each tool, a first-build verification step, and troubleshooting for common issues. Come back here once `./gradlew :ratingbar-cmp:desktopTest` passes on your machine — that's your signal that the environment is ready.

---

## Project Structure

```
ratingbar-cmp/                    # Repo root (Gradle root project: ratingbar-cmp-parent)
├── ratingbar-cmp/                # :ratingbar-cmp — the publishable library
│   ├── src/
│   │   ├── commonMain/           # All library code (Kotlin/Compose, shared across platforms)
│   │   └── commonTest/           # Unit tests (run on desktop JVM)
│   ├── api/                      # Binary compatibility baseline
│   └── build.gradle.kts          # Library build config + publishing metadata
├── samples/                      # Sample apps (flat subprojects in a single Gradle build)
│   ├── common/                   # :samples:common — shared SampleApp composable
│   ├── android/                  # :samples:android — Android launcher
│   ├── desktop/                  # :samples:desktop — Desktop launcher
│   ├── ios/                      # :samples:ios — iOS Compose framework
│   ├── ios-app-host/             # Xcode project wrapping the iOS framework (not a Gradle module)
│   └── web/                      # :samples:web — Web/JS launcher
├── scripts/                      # CI and release scripts (release-check.sh, etc.)
├── docs/                         # Deeper documentation (API reference, CI guide, roadmap)
├── gradle/                       # Version catalog (libs.versions.toml) + wrapper
├── gradle.properties             # Shared Gradle config
├── detekt.yml                    # Shared static-analysis config
├── settings.gradle.kts           # Declares :ratingbar-cmp and :samples:* subprojects
├── build.gradle.kts              # Root aggregator: shared plugin config
└── .github/                      # Workflows, issue templates, PR template, badges
```

### Library source layout

All library code lives in one flat package under `commonMain` — there are no platform-specific source sets in the library module. Platform-specific behavior (hover previews, scroll wheel input, haptic feedback) is gated through Compose APIs rather than `expect`/`actual`.

```
com.github.anandkumarkparmar.ratingbar
├── RatingBar.kt                  # Public composables and gesture/interaction logic
├── RatingBarState.kt             # RatingBarConfig + RatingBarState value types
├── RatingBarDefaults.kt          # Size, spacing, and shimmer preset constants
├── RatingBarColors.kt            # RatingBarColors + RatingBarDefaults.colors() factory
├── RatingBarStyle.kt             # RatingBarStyle + RatingBarDefaults.style() factory
├── RatingBarAnimations.kt        # RatingBarAnimations + RatingBarDefaults.animations() factory
├── RatingBarBehavior.kt          # RatingBarBehavior + RatingBarDefaults.behavior() factory
├── RatingBarIcons.kt             # Built-in vector icons: Star, Heart, ThumbUp, Circle
├── RatingBarPlaceholder.kt       # Shimmer loading skeleton composable
├── RatingBarStateHelpers.kt      # rememberRatingBarState() / rememberSaveableRatingBarState()
├── RatingInteractionSource.kt    # Enum: Tap, Drag, Keyboard, Scroll
└── FractionalClipShape.kt        # Clip shape for partial star fills
```

---

## Your First Contribution

New to open source? Here's the full path from "I want to help" to "my PR is merged."

### 1. Find something to work on

- Browse the [open issues](https://github.com/anandkumarkparmar/ratingbar-cmp/issues) — look for ones labeled `good first issue` or `help wanted`.
- Or pick a rough edge you hit yourself: a typo in docs, a confusing error message, a missing sample.
- If you're unsure whether something is worth a PR, open an issue first and ask.

### 2. Claim the work

Comment on the issue with "I'd like to work on this." Wait for a maintainer to acknowledge before starting major work — that prevents two people from duplicating effort. For small fixes (typos, doc clarity), you can skip this step and go straight to a PR.

### 3. Fork and clone

On GitHub, click **Fork** at the top-right of the repo page. Then clone your fork locally and add the upstream remote:

```bash
git clone https://github.com/<your-username>/ratingbar-cmp.git
cd ratingbar-cmp
git remote add upstream https://github.com/anandkumarkparmar/ratingbar-cmp.git
```

### 4. Create a branch

```bash
git checkout -b fix/typo-in-hover-kdoc
```

Use the branch-naming format described in [Development Workflow](#development-workflow).

### 5. Confirm your setup works before touching anything

```bash
./gradlew :ratingbar-cmp:desktopTest
```

If this passes, you're ready to make changes. If it fails, see [SETUP.md § Troubleshooting](SETUP.md#troubleshooting).

### 6. Make your changes

- Keep the scope small — one PR, one concern.
- Match the existing code style (Kotlin official).
- Add tests if you're adding logic.
- Update docs if you're changing behavior.

### 7. Run validation locally

```bash
./gradlew :ratingbar-cmp:desktopTest     # Unit tests
./gradlew :ratingbar-cmp:detekt          # Static analysis
./gradlew :ratingbar-cmp:apiCheck        # Public API compatibility (only if you touched public API)
```

### 8. Commit with a Conventional Commits message

```bash
git add <files>
git commit -m "fix: correct typo in hover preview KDoc"
```

See [Commit Messages](#commit-messages) for the full format.

### 9. Push and open a PR

```bash
git push origin fix/typo-in-hover-kdoc
```

Then go to GitHub and open a pull request against `main`. The PR template will prompt you for a summary, testing notes, and any migration guidance.

### 10. Respond to review

- A maintainer will review within a few days.
- Expect some back-and-forth — it's collaboration, not criticism.
- Push follow-up commits to the same branch; they'll show up in the PR automatically.

---

## Ways to Contribute

### Bug reports

Open an issue using the **Bug Report** template with:

- Platform (Android / Desktop / iOS / Web)
- Kotlin and Compose Multiplatform versions
- A minimal reproduction (snippet or sample branch)
- Expected vs. actual behavior

### Feature suggestions

Open an issue using the **Feature Request** template with:

- A clear use case — what are you trying to do?
- A proposed API shape (if you have one in mind)
- A non-breaking migration approach (if applicable)

### Code contributions

Follow the ten-step flow in [Your First Contribution](#your-first-contribution). For subsequent contributions, the short version is: branch, change, test, commit, push, PR.

---

## Development Workflow

### Branch naming

Target branch: `main`. Branch names should use one of these prefixes:

- `feat/<topic>` — a new feature
- `fix/<topic>` — a bug fix
- `docs/<topic>` — documentation-only changes
- `refactor/<topic>` — code changes that don't alter behavior
- `test/<topic>` — test-only changes
- `build/<topic>` or `ci/<topic>` — build system or CI changes

### Commit messages

This project uses [Conventional Commits](https://www.conventionalcommits.org/). Commit subjects follow the format:

```
<type>: <short description in imperative, lowercase, no period>
```

Accepted types:

| Type | Use for |
|---|---|
| `feat` | A new feature visible to library users |
| `fix` | A bug fix |
| `docs` | Documentation changes only |
| `refactor` | Code changes that neither fix a bug nor add a feature |
| `test` | Adding or correcting tests |
| `build` | Changes to the build system, dependencies, or publishing |
| `ci` | Changes to CI configuration or scripts |
| `perf` | Performance improvements |
| `chore` | Maintenance work (e.g., version bumps in `libs.versions.toml`) |

Examples from the real commit history:

```
feat: refactor project structure to use a single Gradle build and flatten samples
fix: correct stepped rounding on platforms with inexact float modulo
docs: update demo asset filenames and styling in README
refactor: flatten package structure, update to 0.5.0, and add tests
build: update JitPack coordinates and enhance sample build flexibility
```

If the commit introduces a breaking change, add `!` after the type and a `BREAKING CHANGE:` footer:

```
feat!: regroup RatingBar parameters into value types

BREAKING CHANGE: The star overload now takes config, style, animations,
and behavior objects instead of individual parameters. See the 0.4.0
migration guide in CHANGELOG.md.
```

### Pre-PR checklist

Before opening a PR, run the library validation and sample build commands:

```bash
# Library: compile Android + run Desktop/JS tests + lint + API check
./gradlew :ratingbar-cmp:compileAndroidMain :ratingbar-cmp:desktopTest :ratingbar-cmp:jsTest :ratingbar-cmp:detekt :ratingbar-cmp:apiCheck

# Samples: build all non-iOS platforms
./gradlew :samples:android:assembleDebug :samples:desktop:desktopJar :samples:web:jsBrowserDistribution
```

These commands mirror what CI runs on non-macOS agents, so if they pass locally, CI will almost certainly pass too. On macOS, you can additionally run `./gradlew :ratingbar-cmp:iosSimulatorArm64Test` to cover iOS.

Then confirm:

- [ ] The change is scoped and focused on one concern.
- [ ] No unrelated refactoring.
- [ ] Docs updated if behavior changed.
- [ ] Builds pass locally.
- [ ] Tests added for new logic.
- [ ] Commit messages follow Conventional Commits.

---

## Running Tests

All library tests run on the desktop JVM — there are no Compose UI tests, only pure logic tests in `commonTest`.

### All non-iOS tests (fastest — run this locally by default)

```bash
./gradlew :ratingbar-cmp:compileAndroidMain :ratingbar-cmp:desktopTest :ratingbar-cmp:jsTest
```

This compiles the Android target and runs the full commonTest suite on both Desktop and JS — no iOS, no Xcode, no simulator. On Linux or Windows, this is the maximum library validation you can run locally.

### Per-platform

```bash
# Desktop (runs all commonTest logic — this is the primary test surface)
./gradlew :ratingbar-cmp:desktopTest

# Android compile check (no runtime tests — AGP 9.x limitation)
./gradlew :ratingbar-cmp:assembleUnitTest

# Web / JS
./gradlew :ratingbar-cmp:jsTest

# iOS simulator (requires macOS + Xcode)
./gradlew :ratingbar-cmp:iosSimulatorArm64Test
```

### Pre-release validation

Before tagging a release, run the comprehensive validation script:

```bash
./scripts/release-check.sh
./scripts/release-check.sh --skip-ios       # On non-macOS machines
./scripts/release-check.sh --skip-samples   # Skip sample app builds
```

This runs through 11 steps — compile, test, lint, API check, iOS framework build, Maven local publish, and artifact size report.

---

## Coding Guidelines

- **Keep the public API state-hoisted and predictable.** Callers manage state and pass `value` + `onValueChange` — the library never owns mutable state.
- **Prefer backward-compatible changes.** Breaking changes require a minor version bump and a migration guide in CHANGELOG.
- **Avoid heavy dependencies.** The library is zero-dependency by design (beyond Compose Multiplatform itself). Don't add libraries without discussion.
- **Keep multiplatform parity where feasible.** If a feature only makes sense on one platform (e.g., haptic feedback on Android), document it and make it a no-op everywhere else.
- **Remove stale comments and dead code** when touching files.
- **No `TODO` or `FIXME` comments in PR-ready code.** If something's incomplete, open an issue instead.

### Code style

- Kotlin official code style (`kotlin.code.style=official` in `gradle.properties`).
- KDoc is required on all public API elements.
- Parameter names should be descriptive, not abbreviated.
- Detekt runs with `maxIssues: 0` — all warnings are errors.

---

## Troubleshooting

Setup, build, and environment issues are documented in [SETUP.md § Troubleshooting](SETUP.md#troubleshooting). It covers JDK version mismatches, missing Android SDK, iOS linking OOMs, detekt report navigation, `apiCheck` CI failures, and more.

---

## Getting Help

- **Questions, ideas, or general discussion:** open a thread in [GitHub Discussions](https://github.com/anandkumarkparmar/ratingbar-cmp/discussions). That's the right place for "how do I..." questions, design discussions, or sharing how you're using the library.
- **Bug reports:** use the Bug Report issue template.
- **Feature requests:** use the Feature Request issue template, or start a Discussion first if you want to gauge interest before writing it up.
- **Security concerns:** see [Code of Conduct](CODE_OF_CONDUCT.md#reporting) for the private reporting channel — security issues and conduct issues use the same channel so they don't end up in public issues.
- **PR review turnaround:** expect a first response within a few days. If your PR hasn't been touched in a week, it's fine to gently ping with a comment.

---

## Release Notes Support

If your PR changes user-facing behavior, include in the PR description:

- A short summary of the change.
- Before/after behavior.
- Any migration notes for existing users.

This makes the release-notes generation much smoother at tag time.

---

## Shoutouts (Adopters)

Using this library in a shipped app or project? Open an issue titled **"Shoutout: <Your App Name>"** with:

- App or project name
- Platform(s)
- Short usage note
- App/repo link (optional)
- Logo or screenshot (optional)

These help other contributors see the library in the wild.

---

## Related Documentation

- [Setup Guide](SETUP.md) — install prerequisites, clone, run the samples
- [Usage Examples](docs/USAGE.md) — common patterns with code snippets
- [Live API Docs](https://anandkumarkparmar.github.io/ratingbar-cmp/) — auto-generated reference for every type
- [Roadmap](docs/ROADMAP.md) — what might come next
- [Changelog](CHANGELOG.md) — what's already shipped
- [Publishing Checklist](docs/PUBLISHING_CHECKLIST.md) — release process
- [CI/CD Guide](docs/CI_GUIDE.md) — CI/CD workflow details
- [Code of Conduct](CODE_OF_CONDUCT.md)
