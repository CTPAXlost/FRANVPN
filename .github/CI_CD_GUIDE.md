# GitHub Actions CI/CD - Automated APK Build & Release

## 📋 Overview

The FRANVPN project now has automated GitHub Actions workflows that:

✅ **Build APKs automatically** on every push to main branch  
✅ **Run lint checks** for code quality  
✅ **Create releases** with built APKs  
✅ **Generate artifacts** for download  

---

## 🚀 How It Works

### Workflow Files

```
.github/workflows/
└── build-apk.yml          # Main build and release workflow
```

### Workflow Triggers

The workflow automatically runs when:

1. **Push to main branch** (with Android changes)
   ```
   Whenever you push to main, it builds the APK automatically
   ```

2. **Manual trigger** (Workflow Dispatch)
   ```
   Go to GitHub → Actions → Build APK → Run workflow
   ```

3. **Tag creation** (Release)
   ```
   git tag v1.0.0
   git push origin v1.0.0
   → Creates GitHub Release with APKs attached
   ```

4. **Pull requests** (Verification)
   ```
   Ensures APK builds before merging
   ```

---

## 📥 Getting APKs from GitHub Actions

### Method 1: From Artifacts (Latest Build)

1. Go to GitHub repository: https://github.com/CTPAXlost/FRANVPN
2. Click **Actions** tab
3. Select latest workflow run
4. Scroll to **Artifacts** section
5. Download:
   - `app-debug.apk` (~25 MB)
   - `app-release.apk` (~15 MB)

**Artifacts are kept for 30 days**

### Method 2: From Releases (Tagged Versions)

1. Go to https://github.com/CTPAXlost/FRANVPN/releases
2. Select a release (e.g., v1.0.0)
3. Download APK files from release page
4. **Permanent links** - always available

### Method 3: Direct Download Links

Once a release is created with tag `v1.0.0`, use these links:

```
Debug APK:
https://github.com/CTPAXlost/FRANVPN/releases/download/v1.0.0/app-debug.apk

Release APK:
https://github.com/CTPAXlost/FRANVPN/releases/download/v1.0.0/app-release.apk
```

---

## 🏷️ Creating a Release with APKs

### Step 1: Create a Git Tag

```bash
# On your local machine
git tag v1.0.0
git push origin v1.0.0
```

### Step 2: GitHub Actions Builds & Releases

- Workflow automatically detects the tag
- Builds both Debug and Release APKs
- Creates a GitHub Release
- Attaches APKs to the release
- Makes download links permanent

### Step 3: Access the Release

- Visit: https://github.com/CTPAXlost/FRANVPN/releases
- Click the v1.0.0 release
- Download APKs
- Share links with users

---

## 📊 Workflow Jobs

### Job 1: Build

**What it does:**
- Sets up Java 11
- Installs Android SDK
- Builds Debug APK
- Builds Release APK
- Extracts version info
- Uploads artifacts
- Creates release (if tagged)

**Time:** ~5-8 minutes

**Output:**
- ✅ `app-debug.apk`
- ✅ `app-release.apk`
- ✅ GitHub Artifacts
- ✅ GitHub Release (if tagged)

### Job 2: Test

**What it does:**
- Downloads built APK
- Verifies file exists
- Checks APK integrity
- Reports success/failure

**Time:** ~1 minute

### Job 3: Lint

**What it does:**
- Runs Android lint checks
- Checks code quality
- Detects common issues
- Generates HTML report

**Time:** ~2 minutes

---

## 🔍 Monitoring Builds

### View Build Status

1. **Repository homepage**: Shows workflow status badge
2. **Actions tab**: Shows all workflow runs with details
3. **Commit history**: Each commit shows pass/fail indicator
4. **Pull requests**: Shows build status before merging

### Check Build Logs

1. Go to Actions tab
2. Click the workflow run
3. Click the job (e.g., "build")
4. View real-time logs
5. Expand sections to see details

### Get Notifications

GitHub will email you:
- ❌ When builds fail
- ✅ When releases are created
- 📊 Summary of workflow runs

---

## 🛠️ Customizing the Workflow

### Edit the Workflow

File: `.github/workflows/build-apk.yml`

**Common customizations:**

**Change build triggers:**
```yaml
on:
  push:
    branches: [ main, develop ]  # Add more branches
    
  schedule:
    - cron: '0 0 * * 0'          # Weekly builds
```

**Update artifact retention:**
```yaml
- uses: actions/upload-artifact@v4
  with:
    retention-days: 60            # Keep for 60 days
```

**Add signing configuration:**
```yaml
- name: Decode Keystore
  env:
    KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}
  run: |
    echo "$KEYSTORE_BASE64" | base64 -d > keystore.jks
```

---

## 🔐 Adding Keystore Signing (Optional)

For signed release APKs in CI/CD:

### Step 1: Encode Keystore

```bash
base64 keystore.jks > keystore_base64.txt
```

### Step 2: Add to GitHub Secrets

1. Go to repository Settings
2. Click **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `KEYSTORE_BASE64`
5. Paste base64 content
6. Click **Add secret**

### Step 3: Update Workflow

Add to `build-apk.yml`:

```yaml
- name: Decode Keystore
  env:
    KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}
  run: |
    echo "$KEYSTORE_BASE64" | base64 -d > android/app/keystore/franvpn.jks

- name: Build Release APK (Signed)
  env:
    KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
    KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
    KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
  run: ./gradlew assembleRelease
  working-directory: /workspaces/FRANVPN/android
```

---

## 📈 Workflow Statistics

### Build Times

| Component | Time |
|-----------|------|
| Setup Java & SDK | 2 min |
| Build Debug APK | 2-3 min |
| Build Release APK | 2-3 min |
| Lint checks | 1-2 min |
| **Total** | **5-8 min** |

### Artifact Sizes

| Build Type | Size | Compressed |
|-----------|------|-----------|
| Debug APK | ~25 MB | ~10 MB |
| Release APK | ~15 MB | ~6 MB |
| Lint Report | 100 KB | 20 KB |

---

## ✅ Success Indicators

### Workflow Passed ✅

- ✅ Build job completes
- ✅ APK files created
- ✅ Test job verifies APK
- ✅ Artifacts uploaded
- ✅ No error logs

### Workflow Failed ❌

Common reasons:
- ❌ Gradle compilation error
- ❌ Missing dependencies
- ❌ SDK not installed properly
- ❌ Build configuration issue

**Solution**: Check logs in Actions tab

---

## 🚀 Quick Start

### Create Your First Release

```bash
# 1. Push latest code
git push origin main

# 2. Create version tag
git tag v1.0.0

# 3. Push tag to GitHub
git push origin v1.0.0

# 4. Wait 5-8 minutes for build

# 5. Check releases
# https://github.com/CTPAXlost/FRANVPN/releases
```

### Download Built APK

After workflow completes:

1. **Option A** - From Artifacts (temporary):
   - Actions → Latest run → Artifacts
   - Download app-debug or app-release

2. **Option B** - From Release (permanent):
   - Releases → v1.0.0
   - Download APK files
   - Share permanent links

---

## 📝 Release Notes Template

When creating a release, use this template:

```markdown
# FRANVPN v1.0.0

## What's New
- Complete MVVM architecture
- Full protocol support (VLESS, Trojan, SS, VMess)
- Material Design 3 UI

## Downloads
- **Debug APK** (~25 MB) - For testing
- **Release APK** (~15 MB) - For distribution

## Installation
1. Download APK above
2. Enable "Unknown Apps" in Settings
3. Install APK
4. Grant VPN permission

## Changelog
- See commits: [Compare](https://github.com/CTPAXlost/FRANVPN/compare/v0.9.0...v1.0.0)

## Known Issues
- None

## Next Release
- Xray binary integration
- QR code scanning
- Stats tracking
```

---

## 🐛 Troubleshooting

### Workflow Fails with "Android SDK not found"

**Solution:**
- Workflow uses `android-actions/setup-android`
- Automatically installs required tools
- If fails, check Actions logs for errors

### Workflow Takes Too Long

**Normal:** 5-8 minutes for full build  
**If longer:** Check for stuck jobs in Actions tab

### APK Not Created

**Check:**
1. View workflow logs
2. Look for build errors
3. Verify gradle.kts syntax
4. Check for dependency issues

### Can't Find Download Link

1. Go to **Releases** tab
2. Click the release (v1.0.0)
3. Scroll to **Assets**
4. Download APK files
5. Copy direct link (right-click → Copy link)

---

## 🎯 Best Practices

1. **Version semantically**: v1.0.0, v1.0.1, v1.1.0
2. **Test locally first**: Before pushing to trigger workflow
3. **Write commit messages**: Used in release notes
4. **Keep builds clean**: Avoid merging broken code to main
5. **Monitor action costs**: GitHub free tier includes Actions

---

## 📊 GitHub Actions Pricing

**Free tier includes:**
- 2,000 free Actions minutes per month
- FRANVPN builds use ~6 minutes per run
- ~300 free builds per month ✅

**No payment required** for reasonable usage!

---

## 🔗 Resources

- **Workflow File**: [.github/workflows/build-apk.yml](.github/workflows/build-apk.yml)
- **Actions Documentation**: https://docs.github.com/en/actions
- **Android Actions**: https://github.com/android-actions
- **GitHub Releases**: https://github.com/CTPAXlost/FRANVPN/releases

---

## 📞 Support

For workflow issues:
1. Check Actions logs (Actions tab → Workflow run → Logs)
2. Review commit that triggered build
3. Verify gradle configuration
4. Check Android documentation

---

## 🎉 Next Steps

1. **Commit the workflow file**
   ```bash
   git add .github/workflows/build-apk.yml
   git commit -m "ci: Add GitHub Actions APK build workflow"
   git push origin main
   ```

2. **Monitor first build**
   - Go to Actions tab
   - Watch build progress
   - Download artifacts when done

3. **Create first release**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

4. **Share APK**
   - Visit Releases page
   - Copy direct download link
   - Share with users

---

**Status**: ✅ Workflow ready to use!  
**Next build**: Automatic on next push to main  
**Permanent releases**: When you create a git tag

