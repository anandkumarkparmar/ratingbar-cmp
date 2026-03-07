# Publishing Checklist

Use this checklist whenever you cut a new public release from `main`. Each step references the file or workflow involved.

---

## 1. Prep the release branch

- Ensure `main` contains every change you want to ship; land and review pending PRs first.
- Confirm the repo is clean (`git status`) and CI is green on the latest commit.

## 2. Update versioning and docs

- Bump the library version in [build.gradle.kts](../build.gradle.kts) (`allprojects { version = "..." }`). Keep the `0.x.y` semantic pattern.
- Audit README and docs for accuracy. Update badges, feature lists, or usage snippets if the API changed.
- Update [changelog.md](changelog.md) with the new version's changes.
- If the roadmap needs updating, refresh [roadmap.md](roadmap.md).

## 3. Validate locally before tagging

Run the full build to catch regressions:

```bash
./gradlew clean build --stacktrace
```

Exercise the samples if your change affects a specific platform:

```bash
./gradlew :samples:android:installDebug
./gradlew :samples:desktop:run
./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
./gradlew :samples:web:jsBrowserDevelopmentRun
```

Run the JitPack publication dry-run:

```bash
./gradlew :ratingbar-cmp:publishToMavenLocal -x test --stacktrace
```

Build the Android release artifact:

```bash
./gradlew :ratingbar-cmp:assembleRelease -x test --stacktrace
```

Alternatively, use the release-check script for a comprehensive pre-release validation:

```bash
./scripts/release-check.sh [--skip-ios] [--skip-samples]
```

## 4. Enforce artifact-size budgets

Generate the size matrix:

```bash
chmod +x scripts/report-artifact-sizes.sh
./scripts/report-artifact-sizes.sh --enforce
```

Review the generated report at `.github/artifact-size-report.md` and the badge JSONs in `.github/badges/` to confirm you are below the enforced limits:

| Artifact | Budget |
|---|---|
| Android AAR | 51,200 bytes |
| Total published | 5,242,880 bytes |

## 5. Smoke-test iOS host integration (recommended)

From `samples/ios-app-host`, rebuild the shared framework and Xcode shell (see [running-samples.md](running-samples.md) for instructions).

Launch the simulator via Xcode and verify touch + keyboard accessibility.

## 6. Commit and open a release PR

- Commit the version bump, doc updates, and refreshed artifact-size assets.
- Include regenerated GIFs or screenshots if visuals changed.
- Request review; wait for CI to finish before tagging.

## 7. Tag to trigger Release Prep

Create an annotated tag matching the format enforced in `release.yml`:

```bash
git tag -a v0.x.y -m "ratingbar-cmp v0.x.y"
git push origin v0.x.y
```

This kicks off the **Release Prep** workflow that repeats publication, build, and artifact-size validation on fresh runners.

## 8. Review workflow artifacts

When **Release Prep** finishes:

- Download `artifact-size-report-<tag>` and `release-notes-<tag>` artifacts.
- Confirm the bytes in `.github/artifact-size-summary.env` match expectations.
- Paste the generated `release_notes.md` contents into the GitHub Release draft.

## 9. Verify JitPack availability

- Visit `https://jitpack.io/#anandkumarkparmar/ratingbar-cmp/<version>` and confirm the build succeeds.
- Test the dependency from a sample project:

```kotlin
implementation("com.github.anandkumarkparmar:ratingbar-cmp:<version>")
```

## 10. Post-release tidy-up

- Merge the release PR (if not already merged) and delete the temporary branch.
- Update roadmap and backlog items to reflect shipped features.
- Announce the release and collect user feedback for the next iteration.
