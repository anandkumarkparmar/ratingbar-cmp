# Publishing Checklist

Run through this checklist whenever you cut a new public release of `ratingbar-cmp` from `main`. Each step references the file, command, or workflow involved.

The goal of this document is to be a **pre-flight checklist** — not a tutorial. The heavy lifting is handled by `scripts/release-check.sh` and the `release.yml` workflow; this checklist tells you when to run them and what to verify afterward.

---

## 1. Prep the release branch

- Ensure `main` contains every change you want to ship. Merge or close pending PRs first.
- Confirm the working tree is clean (`git status`) and CI is green on the latest commit.
- Create a release branch for the version bump and final doc updates:

  ```bash
  git checkout -b release/0.x.y
  ```

## 2. Update version and documentation

- Bump the library version in [`gradle.properties`](../gradle.properties) (`libraryVersion=0.x.y`). This is the single source of truth — `ratingbar-cmp/build.gradle.kts` reads it via `property("libraryVersion")`. Keep the semantic `0.x.y` pattern — the release workflow validates it.
- Update [`CHANGELOG.md`](../CHANGELOG.md) — add an entry for the new version using the standard `Added` / `Changed` / `Removed` / `Fixed` / `Deprecated` / `Security` categories.
- Add a comparison reference link at the bottom of `CHANGELOG.md` so the version heading links to the GitHub compare view.
- Audit [`README.md`](../README.md) for any version-specific content (installation snippet, feature lists, migration notes) and update it.
- Update [`ROADMAP.md`](ROADMAP.md) if this release ships any items from it — remove them from the forward-looking lists.
- Refresh demo GIFs or screenshots if visual behavior changed (`assets/demos/`).

## 3. Run pre-release validation

The `release-check.sh` script runs the full compile + test + lint + API compatibility + artifact size pipeline in one pass. Use it as the authoritative "am I ready to tag?" check:

```bash
./scripts/release-check.sh                # Full run — macOS only, includes iOS
./scripts/release-check.sh --skip-ios     # Linux or Windows
./scripts/release-check.sh --skip-samples # Skip sample app builds
```

Exit code `0` means every step passed. Non-zero means fix the reported step and re-run. The script mirrors what CI runs on a release tag, so a clean local pass strongly implies CI will pass too.

## 4. Enforce artifact-size budgets

The release-check script runs artifact size enforcement as its final step, but you can re-run it standalone if you're investigating a size regression:

```bash
chmod +x scripts/report-artifact-sizes.sh
./scripts/report-artifact-sizes.sh --enforce
```

Review the generated outputs:

- `.github/artifact-size-report.md` — human-readable summary
- `.github/badges/*.json` — badge JSONs consumed by shields.io in README

Enforced budgets:

| Artifact | Budget |
|---|---|
| Android AAR | 102,400 bytes (100 KB) |
| Total published | 5,242,880 bytes (5 MB) |

If you're over budget, profile the library with `./gradlew :ratingbar-cmp:bundleAndroidMainAar` and inspect the output for unexpected dependencies.

## 5. Smoke-test iOS host integration (recommended)

From the `samples/ios-app-host/` directory, rebuild the shared Compose framework and open the Xcode project:

```bash
./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
open samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj
```

Run the app on an iOS Simulator and verify touch interaction, keyboard accessibility, and basic rendering. See [`SETUP.md § iOS`](../SETUP.md#ios) for the full step-by-step.

This is a manual smoke test — CI covers framework compilation but not end-to-end Xcode-hosted runtime.

## 6. Commit and open a release PR

- Commit the version bump, changelog, and any refreshed assets:

  ```bash
  git add ratingbar-cmp/build.gradle.kts CHANGELOG.md README.md docs/ROADMAP.md
  git commit -m "chore: release 0.x.y"
  ```

- Push and open a PR from `release/0.x.y` to `main` titled `chore: release 0.x.y`.
- Wait for CI to pass. Request review if you want a second pair of eyes before tagging.

## 7. Tag to trigger the release pipeline

Once the release PR is merged, create an annotated tag from `main`:

```bash
git checkout main
git pull
git tag -a 0.x.y -m "ratingbar-cmp 0.x.y"
git push origin 0.x.y
```

**Tag format:** `release.yml` only accepts tags matching `^0\.[0-9]+\.[0-9]+$` — no `v` prefix. Earlier releases (`v0.1.0`, `v0.2.0`) used the prefix; from `0.3.0` onward the convention is no prefix. Tags with a `v` prefix will be rejected by the validation step.

Pushing the tag triggers three automated jobs after `release-prep` validation passes:

| Job | What it does |
|---|---|
| `release-prep` | Validates the tag, runs `publishToMavenLocal`, `apiCheck`, and `detekt`, builds all modules, enforces artifact size budgets, and generates `release_notes.md` |
| `publish-docs` | Builds Dokka HTML and the web demo, deploys both to GitHub Pages (Dokka at `/`, demo at `/demo/`) |
| `create-github-release` | Creates a GitHub Release with the generated release notes and size report attached — no manual steps required |

## 8. Verify the GitHub Release

Once the workflow finishes (check the **Actions** tab), confirm at `https://github.com/anandkumarkparmar/ratingbar-cmp/releases`:

- The new release appears with the correct tag name.
- The release body contains the size snapshot and dependency snippet.
- GitHub's auto-generated "What's Changed" changelog is present (populated from merged PR titles since the previous tag).
- `artifact-size-report.md` is attached as a downloadable file on the release page.

Also verify that the Dokka site at `https://anandkumarkparmar.github.io/ratingbar-cmp/` has updated to reflect the new version, and that the live demo at `https://anandkumarkparmar.github.io/ratingbar-cmp/demo/` still loads.

## 9. Verify JitPack availability

JitPack builds artifacts lazily on first request, so the new version won't be available until someone fetches it:

- Visit `https://jitpack.io/#anandkumarkparmar/ratingbar-cmp/0.x.y` to trigger the first build. Wait for the status indicator to turn green.
- Verify the Gradle module metadata file is served — this is required for KMP variant selection:

  ```bash
  curl -I "https://jitpack.io/com/github/anandkumarkparmar/ratingbar-cmp/ratingbar-cmp/0.x.y/ratingbar-cmp-0.x.y.module"
  # Expect: HTTP 200
  ```

- Test end-to-end consumption by adding the dependency to a throwaway consumer project:

  ```kotlin
  repositories {
      mavenCentral()
      maven("https://jitpack.io")
  }

  dependencies {
      implementation("com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:0.x.y")
  }
  ```

If JitPack's build fails, check the build log linked from their status page — most failures are JDK/Kotlin/Gradle version mismatches that require updating `jitpack.yml`.

## 10. Post-release tidy-up

- Delete the `release/0.x.y` branch locally and on the remote if it wasn't auto-deleted:

  ```bash
  git branch -d release/0.x.y
  git push origin --delete release/0.x.y
  ```

- Update [`ROADMAP.md`](ROADMAP.md) if any additional items were delivered and you forgot in step 2.
- Announce the release — GitHub Discussions, social media, or wherever your user community lives.
- Collect feedback and bug reports for the next iteration.
