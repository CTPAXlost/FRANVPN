# FRANVPN – Build Guide (APK)

## Prerequisites

### System Requirements
- **OS:** Windows, macOS, or Linux
- **Java:** JDK 11+ (for Android build)
- **Android SDK:** API level 21+ (Android 5.0+)
- **Flutter:** Latest stable version (3.13+)

### Installation Steps

#### 1. Install Flutter

**macOS / Linux:**
```bash
git clone https://github.com/flutter/flutter.git
export PATH="$PATH:`pwd`/flutter/bin"
flutter doctor
```

**Windows:**
Download from https://flutter.dev/docs/get-started/install/windows

#### 2. Install Android SDK

**Option A: Using Android Studio**
1. Download [Android Studio](https://developer.android.com/studio)
2. Open SDK Manager (Tools → SDK Manager)
3. Install:
   - Android SDK Platform 33+
   - Android SDK Build-Tools 34.0.0+
   - Android Emulator (optional)

**Option B: Command Line**
```bash
flutter config --android-sdk /path/to/android/sdk
```

#### 3. Accept Android Licenses

```bash
flutter doctor --android-licenses
# Accept all licenses
```

#### 4. Verify Setup

```bash
flutter doctor
# All checks should show green checkmarks
```

---

## Building the APK

### Option 1: Building Release APK (Recommended)

```bash
cd /workspaces/FRANVPN/mobile

# Get dependencies
flutter pub get

# Build release APK
flutter build apk --release

# Output: build/app/outputs/flutter-apk/app-release.apk
```

### Option 2: Building Debug APK

```bash
cd /workspaces/FRANVPN/mobile

# Get dependencies
flutter pub get

# Build debug APK (faster, larger file)
flutter build apk --debug

# Output: build/app/outputs/flutter-apk/app-debug.apk
```

### Option 3: Building Split APKs (for smaller downloads)

```bash
flutter build apk --split-per-abi --release

# Output:
# build/app/outputs/flutter-apk/app-armeabi-v7a-release.apk
# build/app/outputs/flutter-apk/app-arm64-v8a-release.apk
# build/app/outputs/flutter-apk/app-x86-release.apk
# build/app/outputs/flutter-apk/app-x86_64-release.apk
```

---

## Installing on Device

### Prerequisites
- USB cable
- USB debugging enabled on device
- Drivers installed (for Windows)

### Installation Steps

```bash
# List connected devices
flutter devices

# Install APK on connected device
flutter install

# Or install specific APK file
adb install build/app/outputs/flutter-apk/app-release.apk
```

---

## Testing

### Run on Emulator

```bash
# Start Android emulator first, then:
flutter run

# Or specify release mode
flutter run --release
```

### Run on Physical Device

```bash
# Connect device via USB
adb devices

# Run app
flutter run
```

---

## Signing Release APK

### Generate Keystore

```bash
keytool -genkey -v -keystore ~/franvpn-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias franvpn
```

### Create Key Properties

Create `android/key.properties`:
```properties
storePassword=<keystore_password>
keyPassword=<key_password>
keyAlias=franvpn
storeFile=/path/to/franvpn-key.jks
```

### Build Signed APK

```bash
flutter build apk --release --verbose
```

The signed APK will be at: `build/app/outputs/flutter-apk/app-release.apk`

---

## Troubleshooting

### Issue: "Flutter SDK not found"

**Solution:**
```bash
flutter config --android-sdk /path/to/android/sdk
flutter config --android-toolchain-dir /path/to/android
```

### Issue: Gradle build fails

**Solution:**
```bash
cd mobile/android
./gradlew clean
./gradlew build
```

### Issue: "NDK not found"

**Solution:**
```bash
flutter doctor
# Install suggested NDK version via Android Studio SDK Manager
```

### Issue: APK too large

**Solution:**
```bash
# Use split APKs (by ABI)
flutter build apk --split-per-abi --release
```

---

## Build Configuration

### Minimum SDK Version
Currently set to: **API 21 (Android 5.0)**

To change, edit `android/app/build.gradle`:
```gradle
minSdkVersion 21  // Change this value
```

### Target SDK Version
Currently set to: **API 34 (Android 14)**

```gradle
targetSdkVersion 34  // Change this value
```

### App Name & Package

Edit `pubspec.yaml`:
```yaml
name: franvpn_app
```

Edit `android/app/build.gradle`:
```gradle
applicationId "com.franvpn.app"
```

---

## App Store Release

### Preparation

1. **Create Google Play Developer Account**
   - Cost: $25 one-time fee
   - Visit: https://play.google.com/console

2. **Prepare App Listing**
   - App name, description, screenshots
   - Privacy policy
   - Target audience

3. **Build Production APK**
   ```bash
   flutter build apk --release --split-per-abi
   ```

4. **Sign APK** (see above)

5. **Upload to Play Store**
   - Go to Play Console
   - Create new app
   - Upload APK
   - Fill in app details
   - Submit for review (typically 1-2 hours)

---

## File Locations

### Build Output
```
mobile/
├── build/
│   └── app/
│       └── outputs/
│           └── flutter-apk/
│               ├── app-release.apk
│               ├── app-debug.apk
│               └── app-*-release.apk (split APKs)
```

### Source Code
```
mobile/
├── lib/
│   ├── main.dart
│   ├── screens/
│   │   └── home_screen.dart
│   ├── widgets/
│   │   ├── connection_toggle.dart
│   │   ├── server_card.dart
│   │   └── quick_stats.dart
│   └── theme/
│       └── app_theme.dart
├── pubspec.yaml
└── android/
    ├── build.gradle
    ├── settings.gradle
    └── app/
        ├── build.gradle
        └── src/
            └── main/
                ├── AndroidManifest.xml
                └── kotlin/
                    └── com/franvpn/app/
                        └── MainActivity.kt
```

---

## Performance Optimization

### Reduce APK Size

```bash
# Use split APKs
flutter build apk --split-per-abi --release

# Enable tree-shaking
flutter build apk --release --tree-shake-icons

# Check APK size
cd build/app/outputs/flutter-apk
ls -lh app-*.apk
```

### Typical Sizes
- **Debug APK:** ~50-80 MB
- **Release APK:** ~30-40 MB
- **ARM64 APK:** ~25-30 MB
- **ARMv7 APK:** ~20-25 MB

---

## Next Steps

1. **Install on Device**
   - Build APK
   - Install via USB
   - Test all features

2. **Prepare for Play Store**
   - Generate signed APK
   - Create app listing
   - Upload to Play Console

3. **Gather Screenshots**
   - Home screen (connection active)
   - Server selection
   - Settings
   - Statistics

4. **Write Store Description**
   - Feature highlights
   - Privacy assurances
   - Performance benefits

---

## Build Commands Reference

```bash
# Get dependencies
flutter pub get

# Build debug APK
flutter build apk --debug

# Build release APK
flutter build apk --release

# Build split APKs
flutter build apk --split-per-abi --release

# Run on device
flutter run --release

# Run on emulator
flutter run

# Clean build
flutter clean && flutter pub get

# Install on device
adb install build/app/outputs/flutter-apk/app-release.apk

# Uninstall from device
adb uninstall com.franvpn.app
```

---

## Support

**Issues?** Check:
1. [Flutter Documentation](https://flutter.dev/docs)
2. [Android Documentation](https://developer.android.com/docs)
3. [Gradle Documentation](https://gradle.org/docs)

---

**Last Updated:** January 15, 2026  
**FRANVPN Version:** 1.0  
**Status:** Ready to Build
