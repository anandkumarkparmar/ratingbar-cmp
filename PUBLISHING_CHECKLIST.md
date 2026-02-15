# ratingbar-cmp Publish Checklist

Use this checklist whenever you cut a new public release from `main`. Each step references the file or workflow that must be touched so you can quickly verify nothing is missed.

## 1. Prep the release branch
- Ensure `main` contains every change you want to ship; land and review pending PRs first.
- Confirm the repo is clean (`git status`) and CI is green on the latest commit.

## 2. Update versioning + docs
- Bump the library version exposed to consumers in [build.gradle.kts](build.gradle.kts#L9-L12). Keep the `0.x.y` semantic pattern.
- Audit README and docs for accuracy. Update [README.md](README.md) hero badges, feature lists, or usage snippets if the API changed.
- If you maintain a change log or roadmap entry, update it alongside the release PR.

## 3. Validate locally before tagging
- Run the full build to catch regressions:
  ```bash
  ./gradlew clean build --stacktrace
  ```
- Exercise the samples if your change affects a specific platform:
  ```bash
  ./gradlew :samples:android:installDebug
  ./gradlew :samples:desktop:run
  ./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
  ./gradlew :samples:web:jsBrowserDevelopmentRun
  ```
- Run the JitPack publication dry-run to ensure Maven metadata stays valid:
  ```bash
  ./gradlew :ratingbar-cmp:publishToMavenLocal -x test --stacktrace
  ```
- Build the Android release artifact that JitPack will publish:
  ```bash
  ./gradlew :ratingbar-cmp:assembleRelease -x test --stacktrace
  ```

## 4. Enforce artifact-size budgets
- Generate the size matrix so badges and budgets remain current:
  ```bash
  chmod +x scripts/report-artifact-sizes.sh
  ./scripts/report-artifact-sizes.sh --enforce
  ```
- Review the generated report at [.github/artifact-size-report.md](.github/artifact-size-report.md) plus the badge JSONs in [.github/badges/](.github/badges/) to confirm you are still below the limits enforced in [build.yml](.github/workflows/build.yml#L52-L88).

## 5. Smoke-test iOS host integration (optional but recommended)
- From `samples/ios-app-host`, run the convenience command noted in [samples/ios-app-host/README.md](samples/ios-app-host/README.md) to rebuild the shared framework and Xcode shell. This ensures Compose for iOS still links after your changes.
- Launch the simulator via Xcode (`sample-ratingbar-cmp` scheme) and swipe/drag the rating bar to verify touch + keyboard accessibility.

## 6. Commit + open a release PR
- Commit the version bump, doc updates, and refreshed artifact-size assets.
- Include regenerated GIFs or screenshots if you updated visuals.
- Request review; wait for CI (`CI` workflow) to finish before tagging.

## 7. Tag to trigger Release Prep
- Create an annotated tag that matches the format enforced in [release.yml](.github/workflows/release.yml#L7-L30), e.g. `git tag -a v0.2.0 -m "ratingbar-cmp v0.2.0"`.
- Push the tag: `git push origin v0.2.0`. This kicks off the **Release Prep** workflow that repeats the publication, build, and artifact-size validation on fresh runners.

## 8. Review workflow artifacts
- When **Release Prep** finishes, download `artifact-size-report-<tag>` and `release-notes-<tag>` artifacts to confirm the bytes logged in `.github/artifact-size-summary.env` match expectations.
- Paste the generated `release_notes.md` contents into the GitHub Release draft if you publish releases manually.

## 9. Verify JitPack availability
- Visit `https://jitpack.io/#anandkumarkparmar/ratingbar-cmp/<version>` and make sure the build succeeds.
- Test the dependency from a sample project by pointing to the new version:
  ```kotlin
  implementation("com.github.anandkumarkparmar:ratingbar-cmp:<version>")
  ```

## 10. Post-release tidy-up
- Merge the release PR (if not already merged) and delete the temporary branch.
- Update ROADMAP or backlog items to reflect the shipped features.
- Announce the release (tweet, Slack, etc.) and collect user feedback for the next iteration.
