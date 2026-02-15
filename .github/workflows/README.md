# GitHub Actions Workflows

## Active Workflows

### 🔵 `ci.yml` - Smart CI Workflow
**Main CI pipeline with intelligent change detection**

- **Triggers**: PR, push to main, manual
- **Features**:
  - Detects what files changed
  - Runs only affected platform builds
  - Parallel job execution
  - Cost-optimized (70-85% savings)
  - iOS builds only when needed

**See [CI_GUIDE.md](../CI_GUIDE.md) for complete documentation**

### 🟢 `release.yml` - Release Workflow
**Handles releases and publishing**

- **Triggers**: Release published
- **Actions**: Publish artifacts

## Quick Reference

### During Development
```bash
# Create draft PR (no builds)
gh pr create --draft

# Ready for review (quick builds)
gh pr ready

# Ready to merge (full builds)
gh pr edit --add-label "ready-to-merge"
```

### Manual Testing
```bash
# Run full build manually
gh workflow run ci.yml -f full_build=true

# Check recent runs
gh run list --limit 10
```

### What Builds When

| Change | Builds |
|--------|--------|
| `ratingbar-cmp/android/` | Android library only |
| `ratingbar-cmp/commonMain/` | All libraries (Android, Desktop, Web) |
| `samples/web/` | Web sample only |
| `README.md` | Nothing (skipped) |
| Label: `ready-to-merge` | Everything (full build) |

## Status Checks

Required checks for merging:
- ✅ `validate` - Quick compilation check
- ✅ `library-android` - Android library build
- ✅ `library-desktop` - Desktop library build  
- ✅ `library-web` - Web library build
- ✅ `build-summary` - Overall status

Optional checks (conditional):
- 🟦 `library-ios` - Only on ready-to-merge or main
- 🟦 `jitpack-check` - Only on ready-to-merge or main

## Documentation

Comprehensive guide: **[../.github/CI_GUIDE.md](../CI_GUIDE.md)**

Topics covered:
- Architecture overview
- Job flow and dependencies
- Usage patterns
- Performance optimization
- Troubleshooting
- Customization

## Backup Files

- `build.yml.backup` - Original build workflow (deprecated)
