# Contributing to ratingbar-cmp

Thanks for contributing to **ratingbar-cmp**.

Current baseline is **v0.1.0**. We are prioritizing stability, docs quality, and targeted improvements.

---

## Ways to Contribute

### 1) Bug Reports

Open an issue with:
- platform (Android/Desktop/iOS/Web)
- Kotlin + Compose versions
- minimal reproduction
- expected vs actual behavior

### 2) Feature Suggestions

Open an issue with:
- clear use case
- proposed API shape
- non-breaking migration approach (if applicable)

### 3) Code Contributions

```bash
git clone https://github.com/anandkumarkparmar/ratingbar-cmp.git
cd ratingbar-cmp
./gradlew build
```

Before opening PR:

```bash
./gradlew :samples:common:compileKotlinMetadata \
  :samples:android:compileDebugKotlinAndroid \
  :samples:desktop:compileKotlinDesktop \
  :samples:web:compileKotlinJs \
  :samples:ios:compileKotlinIosSimulatorArm64
```

PR checklist:
- [ ] change is scoped and focused
- [ ] no unrelated refactor
- [ ] docs updated if behavior changed
- [ ] builds pass locally

---

## Shoutout Contributions (Adopters)

Using this library in your app/project?

Open an issue titled **"Shoutout: <Your App Name>"** with:
- app or project name
- platform(s)
- short usage note
- app/repo link (optional)
- logo/screenshot (optional)

Verified shoutouts help establish trust and make adoption easier for other teams.

---

## Coding Guidelines

- Keep public API state-hoisted and predictable
- Prefer backward-compatible changes on current release line
- Avoid adding heavy dependencies
- Keep multiplatform parity where feasible
- Remove stale comments and dead code when touching files

---

## Branching

- target branch: `main`
- branch naming: `fix/<topic>`, `feat/<topic>`, `docs/<topic>`

---

## Release Notes Support

If your PR changes user-facing behavior, include:
- short summary
- before/after behavior
- any migration notes
