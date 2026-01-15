# Automatic APK Builder - Monitoring Guide

## Overview

When the Android SDK becomes available in this environment, use the automatic build script to compile the APK files immediately.

## ✅ Quick Start

### Option 1: Run Now (Waits for SDK)
```bash
cd /workspaces/FRANVPN
chmod +x auto-build-when-sdk-ready.sh
./auto-build-when-sdk-ready.sh
```

This will:
1. Check for Android SDK availability
2. Wait up to 1 hour for SDK to be installed
3. Build APK automatically when SDK is found
4. Create download links

### Option 2: Run in Background
```bash
cd /workspaces/FRANVPN
chmod +x auto-build-when-sdk-ready.sh
nohup ./auto-build-when-sdk-ready.sh > apk-build.log 2>&1 &
```

Monitor with:
```bash
tail -f apk-build.log
```

### Option 3: Run with Terminal
```bash
cd /workspaces/FRANVPN
chmod +x auto-build-when-sdk-ready.sh
./auto-build-when-sdk-ready.sh | tee apk-build.log
```

## 📊 What the Script Does

**Step 1: Monitor for Android SDK**
- Checks every 30 seconds if Android SDK is installed
- Looks for: `android` command, `sdkmanager`, or `$ANDROID_HOME`
- Waits up to 1 hour (configurable)

**Step 2: Build APK Files**
```
Android SDK Found!
  ↓
Setup Java & Gradle
  ↓
Build Debug APK (~25 MB)
  ↓
Build Release APK (~15 MB)
  ↓
Verify APK integrity
  ↓
Create download links
  ↓
Ready for installation!
```

**Step 3: Create Download Information**
- Saves to: `APK_DOWNLOAD_READY.txt`
- Contains APK locations
- Includes installation methods
- Lists next steps

**Step 4: Ready for Download**
Output files:
- `android/app/build/outputs/apk/debug/app-debug.apk`
- `android/app/build/outputs/apk/release/app-release.apk`
- Build logs: `android/build-debug.log`, `android/build-release.log`

## 🎯 Current Status

### SDK Availability
```
Status: ❌ Android SDK NOT AVAILABLE
Next Step: Wait for environment update
```

### When SDK Becomes Available
The script will:
1. Automatically detect SDK installation
2. Start building immediately (no manual action needed)
3. Create `APK_DOWNLOAD_READY.txt` file
4. Output build logs

You can then:
- Download APK files from output directory
- Install on Android device
- Push to GitHub Releases
- Share with users

## 📝 Configuration

To customize the script:

```bash
# Edit auto-build-when-sdk-ready.sh and change:

ANDROID_SDK_CHECK_INTERVAL=30  # Check every 30 seconds (default)
MAX_WAIT_TIME=3600             # Wait up to 1 hour (default)

# Examples:
ANDROID_SDK_CHECK_INTERVAL=10  # Check every 10 seconds (faster)
MAX_WAIT_TIME=7200             # Wait up to 2 hours
```

## 🔍 Monitoring the Build

### Watch in Real-time
```bash
./auto-build-when-sdk-ready.sh
```

### Check Logs
```bash
tail -f android/build-debug.log
tail -f android/build-release.log
```

### Check for Completion
```bash
ls -lh APK_DOWNLOAD_READY.txt
ls -lh android/app/build/outputs/apk/*/app-*.apk
```

## 📥 After Build Completes

### Download APKs
```bash
# Debug APK (for testing)
cp android/app/build/outputs/apk/debug/app-debug.apk ~/Downloads/

# Release APK (for distribution)
cp android/app/build/outputs/apk/release/app-release.apk ~/Downloads/
```

### Install on Device
```bash
# Via ADB
adb install android/app/build/outputs/apk/debug/app-debug.apk

# Or transfer to phone and install manually
```

### Push to GitHub
```bash
cd /workspaces/FRANVPN
git add android/app/build/outputs/apk/
git commit -m "build: Auto-built APK with Android SDK"
git push origin main

# Create release
git tag v1.0.0
git push origin v1.0.0
# → Permanent download link created!
```

### Update Documentation
```bash
# Create APK_DOWNLOAD_READY.txt shows:
# - APK file paths
# - Installation methods
# - Next steps
```

## 🔧 Troubleshooting

### Script Won't Start
```bash
# Make executable
chmod +x auto-build-when-sdk-ready.sh

# Run with bash explicitly
bash auto-build-when-sdk-ready.sh
```

### Android SDK Not Detected
```bash
# Verify SDK installation
echo $ANDROID_HOME
which android
which sdkmanager

# If SDK is installed but not detected, set ANDROID_HOME:
export ANDROID_HOME=/path/to/android/sdk
./auto-build-when-sdk-ready.sh
```

### Build Fails with Gradle Error
```bash
# Check Java installation
java -version

# Verify Gradle wrapper
ls -la android/gradlew

# Manual build to see errors
cd android
./gradlew clean assembleDebug -v
```

### Release APK Signing Fails
```bash
# Release builds need keystore
# Default location: android/app/keystore/release.keystore

# Create keystore if missing
keytool -genkey -v -keystore android/app/keystore/release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias release -storepass android -keypass android

# Then retry build
./auto-build-when-sdk-ready.sh
```

## 📊 Build Output Example

```
[2025-01-15 14:30:45] Starting APK auto-builder...
[2025-01-15 14:30:45] Monitoring for Android SDK availability...
[2025-01-15 14:30:45] Android SDK not found. Retrying in 30s...
[2025-01-15 14:31:15] Android SDK not found. Retrying in 30s...
[2025-01-15 14:31:45] ✅ Android SDK detected!
[2025-01-15 14:31:45] Building FRANVPN APK files...
[2025-01-15 14:31:45] Checking prerequisites...
[2025-01-15 14:32:15] Building Debug APK...
[2025-01-15 14:35:20] ✅ Debug APK built successfully
[2025-01-15 14:35:20] Building Release APK...
[2025-01-15 14:37:45] ✅ Release APK built successfully
[2025-01-15 14:37:45] Verifying APK files...
[2025-01-15 14:37:46] ✅ Debug APK: /workspaces/FRANVPN/android/app/build/outputs/apk/debug/app-debug.apk (25M)
[2025-01-15 14:37:46] ✅ Release APK: /workspaces/FRANVPN/android/app/build/outputs/apk/release/app-release.apk (15M)
[2025-01-15 14:37:47] ✅ Download information saved
[2025-01-15 14:37:47] ============================================
[2025-01-15 14:37:47] ✅ FRANVPN APK BUILD COMPLETE
[2025-01-15 14:37:47] ✅ Debug APK: /workspaces/FRANVPN/android/app/build/outputs/apk/debug/app-debug.apk
[2025-01-15 14:37:47] ✅ Release APK: /workspaces/FRANVPN/android/app/build/outputs/apk/release/app-release.apk
[2025-01-15 14:37:47] ✅ See APK_DOWNLOAD_READY.txt for details
```

## ⏱️ Expected Timing

- SDK Check: 1 second per check
- Initial Wait: 0-30 seconds (if SDK already available)
- Debug Build: 3-5 minutes
- Release Build: 2-3 minutes
- Verification: 10-20 seconds
- **Total: 5-10 minutes** (after SDK becomes available)

## 🔗 Related Files

- [APK_BUILD_GUIDE.md](APK_BUILD_GUIDE.md) — Manual build instructions
- [DOWNLOAD_APK.md](DOWNLOAD_APK.md) — Download & installation guide
- [CI_CD_QUICK_START.md](CI_CD_QUICK_START.md) — GitHub Actions CI/CD
- [android/build.sh](android/build.sh) — Manual build script

## 📞 Support

If the auto-build fails:

1. Check the build logs:
   ```bash
   cat android/build-debug.log
   cat android/build-release.log
   ```

2. Try manual build:
   ```bash
   cd android
   ./gradlew clean assembleDebug
   ```

3. See [APK_BUILD_GUIDE.md](APK_BUILD_GUIDE.md) for detailed instructions

4. Check prerequisites:
   - Java 11+ installed: `java -version`
   - Android SDK installed: `echo $ANDROID_HOME`
   - Gradle wrapper executable: `ls -x android/gradlew`

## ✨ Summary

**Current Status:** ❌ Android SDK not available  
**When SDK Available:** Run `./auto-build-when-sdk-ready.sh`  
**Build Time:** 5-10 minutes after SDK detected  
**Next Step:** Wait for environment update or use GitHub Actions CI/CD  

---

**Note:** This script is designed to work when Android SDK becomes available in the container. Until then, use [GitHub Actions CI/CD](CI_CD_QUICK_START.md) for automated APK builds.
