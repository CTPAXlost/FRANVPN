# FRANVPN APK - Build, Download & Installation Guide

## 📦 APK Overview

**FRANVPN** is a production-ready Android VPN client supporting:
- ✅ **Protocols**: VLESS, Trojan, Shadowsocks, VMess, JSON configs
- ✅ **Architecture**: MVVM + Repository Pattern with Kotlin
- ✅ **Database**: Room ORM with reactive Flow updates
- ✅ **Min SDK**: Android 8.0 (API 26)
- ✅ **Target SDK**: Android 14 (API 34)
- ✅ **Size**: ~15-25 MB (debug), ~12-18 MB (release, after ProGuard optimization)

---

## 🏗️ Building the APK

### Prerequisites

```bash
# Required
- Java 11 or higher
- Gradle 8.1+ (or use ./gradlew wrapper)
- 2GB+ free disk space

# Recommended
- Android SDK (API 34)
- Kotlin 1.9+
```

### Build Steps

#### Option 1: Using Build Script (Recommended)

```bash
cd android/
./build.sh
```

This interactive script will:
1. Verify prerequisites (Java, Gradle)
2. Ask for build type (debug/release)
3. Clean and build APK
4. Display APK location and size

#### Option 2: Using Gradle Directly

**Debug Build (for testing)**
```bash
cd android/
./gradlew clean assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

**Release Build (for distribution)**
```bash
cd android/
./gradlew clean assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

#### Option 3: Build Without Optimization (Faster)

```bash
cd android/
./gradlew clean assembleDebug -x lint -x test
```

### Build Output Locations

| Build Type | Path | Size | Use Case |
|-----------|------|------|----------|
| Debug | `app/build/outputs/apk/debug/app-debug.apk` | ~25 MB | Testing, development |
| Release | `app/build/outputs/apk/release/app-release.apk` | ~15 MB | Distribution, Play Store |
| Bundle | `app/build/outputs/bundle/release/app-release.aab` | ~12 MB | Google Play Store |

---

## 📥 Installation Options

### Option 1: Install from Device (Android Studio)

```bash
cd android/
./gradlew installDebug
```

Requirements:
- Device connected via USB
- USB debugging enabled
- ADB installed

### Option 2: Install via ADB

```bash
adb install -r path/to/app-debug.apk
```

### Option 3: Manual Installation

1. Download APK to device
2. Open file manager
3. Navigate to Downloads folder
4. Tap the APK file
5. Tap "Install"
6. Grant required permissions
7. Launch app

### Option 4: Command-Line Installation (macOS/Linux)

```bash
# Copy APK to device and install
adb push path/to/app-debug.apk /sdcard/Download/
adb shell pm install /sdcard/Download/app-debug.apk
```

---

##  Required Permissions

FRANVPN requires the following permissions:

```xml
<!-- VPN -->
<uses-permission android:name="android.permission.BIND_VPN_SERVICE" />

<!-- Network -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />

<!-- Storage (for config import) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

<!-- Camera (for QR code scanning) -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Foreground Service (for VPN notification) -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE" />

<!-- Boot completion (auto-start) -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

All permissions are requested at runtime on Android 6.0+.

---

## 🔧 Configuration

### App Settings (First Launch)

1. **DNS Configuration**
   - Default: Cloudflare (1.1.1.1, 8.8.8.8)
   - Custom: Set your preferred DNS
   - IPv6: Can be enabled/disabled

2. **VPN Options**
   - Kill Switch: Disconnect if VPN fails
   - Auto-Reconnect: Reconnect on network change
   - Split Tunneling: Bypass VPN for specific apps (optional)

3. **Theme**
   - Dark Mode: Default
   - Red & Black branding

### Adding VPN Configurations

#### Method 1: Import from URI

```
vless://[uuid]@[server]:[port]?security=tls&sni=[host]&type=[transport]
trojan://[password]@[server]:[port]?sni=[host]
ss://[method-password]@[server]:[port]
vmess://[base64-json-config]
```

#### Method 2: Import from Subscription URL

```
Menu → Subscriptions → Add Subscription
URL: https://example.com/subscription
```

#### Method 3: Import from JSON

```json
{
  "protocol": "vless",
  "server": "example.com",
  "port": 443,
  "uuid": "00000000-0000-0000-0000-000000000000",
  "tlsEnabled": true,
  "tlsHost": "example.com",
  "transport": "tcp"
}
```

---

## 🚀 First Run Checklist

- [ ] App installed successfully
- [ ] VPN permission granted
- [ ] At least one VPN config added
- [ ] DNS configured
- [ ] Test connection to ensure working
- [ ] (Optional) Enable kill switch for security
- [ ] (Optional) Enable auto-reconnect

---

## 🐛 Troubleshooting

### Build Fails with "Gradle not found"

```bash
cd android/
./gradlew clean assembleDebug  # Uses wrapper
```

### APK Installation Fails

```bash
# Check device compatibility
adb shell getprop ro.build.version.sdk

# Clear old installation
adb uninstall com.franvpn.app

# Try again
adb install app-debug.apk
```

### Permission Denied Errors

```bash
# Enable USB debugging
Settings → Developer Options → USB Debugging

# Revoke app permissions
adb shell pm clear com.franvpn.app
```

### VPN Connection Fails

1. Check server configuration
2. Verify DNS settings
3. Ensure network connectivity
4. Check Xray binary is present in assets
5. Review app logs: `adb logcat | grep FRANVPN`

### App Crashes

```bash
# View crash logs
adb logcat | grep "E/AndroidRuntime"

# Check Kotlin exceptions
adb logcat | grep "com.franvpn.app"
```

---

## 📊 Build Variants

### Debug Variant

```
Target: Development & Testing
Features:
  - Full logging (Timber)
  - Debuggable
  - Fast build
  - No obfuscation
```

### Release Variant

```
Target: Production & Distribution
Features:
  - ProGuard/R8 obfuscation
  - Resource shrinking
  - Signed with release key
  - Optimized (smaller size)
```

---

## 🔐 Security Considerations

### Keystore Management

```bash
# Generate release keystore (one-time)
keytool -genkey -v -keystore keystore/franvpn.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias franvpn

# Environment variables for signing
export KEYSTORE_PASSWORD="your_password"
export KEY_ALIAS="franvpn"
export KEY_PASSWORD="your_password"
```

### Obfuscation Rules

ProGuard rules are configured in `app/proguard-rules.pro`:
- Keeps VPN service classes public
- Preserves Room database classes
- Keeps Retrofit interfaces
- Preserves exception handling

### Code Signing

Release APK must be:
1. Signed with release keystore
2. Aligned with zipalign
3. Uploaded to Play Store or distributed securely

---

## 📈 Monitoring Build Performance

```bash
# Detailed build timing
./gradlew clean assembleDebug --profile

# Check dependencies
./gradlew dependencies

# Analyze APK size
./gradlew analyzeReleaseBundle
```

---

## 🔗 CI/CD Integration

### GitHub Actions Example

```yaml
name: Build APK

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - run: cd android && ./gradlew clean assembleRelease
      - uses: actions/upload-artifact@v3
        with:
          name: apk
          path: android/app/build/outputs/apk/release/
```

---

## 📦 Distribution

### Option 1: GitHub Releases

```bash
# Create release with APK
gh release create v1.0.0 android/app/build/outputs/apk/release/app-release.apk
```

### Option 2: Firebase App Distribution

```bash
# Requires Firebase project setup
./gradlew appDistributionUploadRelease
```

### Option 3: Google Play Store

```bash
# Build App Bundle
./gradlew clean bundleRelease

# Upload to Play Console
# - https://play.google.com/console
# - Select app → Internal testing → Create Release
```

---

## 📝 Version Updates

To update version:

```kotlin
// android/app/build.gradle.kts
android {
    defaultConfig {
        versionCode = 2        // Increment for each build
        versionName = "1.0.1"  // Semantic versioning
    }
}
```

---

## 🆘 Support & Issues

- **Bug Reports**: GitHub Issues
- **Build Problems**: Check prerequisites and logs
- **Questions**: Refer to ANDROID_README.md
- **Source Code**: Available in `app/src/main/`

---

## ✅ Verification Checklist

After building, verify:

- [x] APK file exists and is non-zero size
- [x] All required permissions present
- [x] VPN service implementation complete
- [x] Protocol parsers functional
- [x] Database schemas initialized
- [x] UI layouts properly defined
- [x] Kotlin compilation successful
- [x] ProGuard rules applied
- [x] APK signed (release only)
- [x] Installation succeeds on target device

---

## 📞 Next Steps

1. **Build the APK**
   ```bash
   cd android && ./build.sh
   ```

2. **Install on device**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Test core features**
   - Add VPN config
   - Connect to VPN
   - Verify traffic routing

4. **Report issues**
   - Collect logs: `adb logcat > logs.txt`
   - Describe reproduction steps
   - Share relevant device info

---

**Last Updated**: January 15, 2026  
**FRANVPN Version**: 1.0.0  
**Minimum Android**: 8.0 (API 26)  
**Target Android**: 14 (API 34)
