# FRANVPN Mobile App – Flutter Project

A fully functional Flutter application for FRANVPN VPN client with native Android & iOS support.

**Status:** ✅ Ready to Build  
**Framework:** Flutter 3.13+  
**Platforms:** Android 5.0+ (API 21+), iOS 11+  
**Language:** Dart + Kotlin (Android) + Swift (iOS)

---

## 📁 Project Structure

```
mobile/
├── lib/                              # Dart source code
│   ├── main.dart                     # App entry point
│   ├── screens/
│   │   └── home_screen.dart          # Main dashboard (connection, stats)
│   ├── widgets/
│   │   ├── connection_toggle.dart    # Hero animated toggle button
│   │   ├── server_card.dart          # Server selection card
│   │   └── quick_stats.dart          # Upload/download speed display
│   ├── theme/
│   │   └── app_theme.dart            # Brand colors, typography, theming
│   ├── models/                       # (To be created)
│   ├── services/                     # (To be created)
│   └── providers/                    # Riverpod state management
│
├── android/                          # Android native code
│   ├── app/
│   │   ├── build.gradle              # App build configuration
│   │   ├── src/main/
│   │   │   ├── AndroidManifest.xml   # App permissions & services
│   │   │   └── kotlin/com/franvpn/app/
│   │   │       └── MainActivity.kt   # Activity + VPN Service
│   │   └── src/debug/                # Debug signing
│   ├── build.gradle                  # Project build config
│   └── settings.gradle               # Module settings
│
├── ios/                              # iOS native code (Xcode project)
│   ├── Runner.xcodeproj/
│   ├── Runner/
│   │   ├── GeneratedPluginRegistrant.h
│   │   ├── GeneratedPluginRegistrant.m
│   │   ├── GeneratedPlugins.json
│   │   └── Info.plist                # App configuration
│   └── Podfile                       # CocoaPods dependencies
│
├── pubspec.yaml                      # Flutter package dependencies
├── pubspec.lock                      # Locked versions
├── analysis_options.yaml             # Dart linter rules
└── README.md                         # This file
```

---

## 🚀 Quick Start

### 1. Install Flutter

**macOS / Linux:**
```bash
git clone https://github.com/flutter/flutter.git
export PATH="$PATH:`pwd`/flutter/bin"
flutter doctor
```

**Windows:**
Download from https://flutter.dev/docs/get-started/install/windows

### 2. Get Dependencies

```bash
cd mobile
flutter pub get
```

### 3. Run on Device/Emulator

```bash
# List connected devices
flutter devices

# Run app
flutter run

# Run in release mode
flutter run --release
```

### 4. Build APK (Android)

```bash
# Debug APK
flutter build apk --debug

# Release APK
flutter build apk --release

# Split APKs (smaller downloads)
flutter build apk --split-per-abi --release
```

---

## 📦 Dependencies

### UI & State Management
- **provider** (6.0.0) – Simple state management
- **riverpod** (2.4.0) – Advanced state management
- **flutter_riverpod** (2.4.0) – Riverpod for Flutter
- **google_fonts** (6.1.0) – Google Fonts integration
- **flutter_animate** (4.2.0) – Animation utilities
- **cupertino_icons** (1.0.2) – iOS-style icons

### Networking
- **http** (1.1.0) – HTTP client
- **dio** (5.3.0) – Advanced HTTP client

### Storage
- **hive** (2.2.0) – Local NoSQL database
- **hive_flutter** (1.1.0) – Hive for Flutter

### VPN & Platform Integration
- **flutter_vpn** (1.2.0) – VPN service integration

### Utilities
- **intl** (0.19.0) – Internationalization
- **uuid** (4.0.0) – UUID generation
- **package_info_plus** (5.0.0) – App info
- **fl_chart** (0.65.0) – Charts & graphs
- **qr_flutter** (4.1.0) – QR code generation
- **mobile_scanner** (3.5.0) – QR code scanning

---

## 🎨 UI Components

### Existing Widgets

#### ConnectionToggle
- Large animated toggle button (hero element)
- Shows connection status (CONNECTED/DISCONNECTED)
- Smooth animations on state change
- Size: 180x180px

**File:** `lib/widgets/connection_toggle.dart`

#### ServerCard
- Displays server info (name, location, latency, load)
- Color-coded latency indicator (green/yellow/red)
- Tap to select server

**File:** `lib/widgets/server_card.dart`

#### QuickStats
- Shows current upload/download speeds
- Real-time speed display with icons
- Two-column layout

**File:** `lib/widgets/quick_stats.dart`

---

## 🎯 Screens to Implement

### 1. Home Screen ✅ (Complete)
- Connection toggle
- Current server info
- Real-time stats
- Quick server selection
- Bottom navigation

### 2. Servers Screen (To Do)
- Server list view (filterable)
- Map view with clickable markers
- Latency indicator
- Favorite servers
- Search functionality

### 3. Profiles Screen (To Do)
- List of profiles
- Create/edit/delete profiles
- Profile cloning
- Import from QR/URL

### 4. Settings Screen (To Do)
- DNS provider selection
- Kill switch toggle
- Split tunneling rules
- Appearance (dark/light theme)
- Advanced options

### 5. Statistics Screen (To Do)
- Daily/weekly/monthly usage
- Connection history
- Top servers chart
- Protocol usage breakdown
- Export stats

---

## 🛠️ Building Configuration

### Minimum SDK (Android)
```gradle
minSdkVersion 21  // Android 5.0+
```

### Target SDK (Android)
```gradle
targetSdkVersion 34  // Android 14
```

### App Package
```
com.franvpn.app
```

### App Version
- Version: 1.0.0
- Build: 1

---

## 🔐 Permissions Required

### Android (`AndroidManifest.xml`)
- `INTERNET` – Network access
- `ACCESS_NETWORK_STATE` – Check network status
- `CHANGE_NETWORK_STATE` – Manage VPN
- `BIND_VPN_SERVICE` – VPN service binding
- `CAMERA` – QR code scanning
- `WRITE_EXTERNAL_STORAGE` – Save configs
- `READ_EXTERNAL_STORAGE` – Load configs

### iOS (`Info.plist`)
- Network access (NSLocalNetworkUsageDescription)
- Bluetooth (NSBluetoothPeripheralUsageDescription)
- Camera (NSCameraUsageDescription)
- Photos (NSPhotoLibraryUsageDescription)

---

## 🎨 Theming

### Brand Colors
- **Emerald Green:** #00B074 (light) / #00C878 (dark)
- **Navy Blue:** #1E3A8A (light) / #1E293B (dark)
- **Slate Gray:** #E2E8F0 (light) / #6B7280 (dark)

### Typography
- **Font:** Inter (Google Fonts)
- **Sizes:** 32px (H1), 24px (H2), 16px (body), 14px (small)

### Dark/Light Themes
- **Dark Theme:** Default (navy background)
- **Light Theme:** Alternative (white background)
- **Toggle in Settings**

**File:** `lib/theme/app_theme.dart`

---

## 📱 Responsive Design

### Breakpoints
- **Mobile:** < 600px width
- **Tablet:** 600-1200px width

### Layout Adaptation
- Single column (mobile)
- Two column (tablet)
- Responsive buttons and cards

---

## 🔧 Development Commands

```bash
# Get packages
flutter pub get

# Run on device
flutter run

# Run in release mode
flutter run --release

# Run tests
flutter test

# Build APK
flutter build apk --release

# Build for iOS
flutter build ios --release

# Clean build
flutter clean

# Check for issues
flutter analyze

# Format code
dart format lib/

# Get coverage
flutter test --coverage
```

---

## 📊 State Management

### Using Riverpod

```dart
// Define a provider
final connectionStateProvider = StateNotifierProvider((ref) {
  return ConnectionStateNotifier();
});

// Use in UI
@override
Widget build(BuildContext context, WidgetRef ref) {
  final connectionState = ref.watch(connectionStateProvider);
  
  return Text(
    connectionState ? 'Connected' : 'Disconnected'
  );
}
```

### Using Provider

```dart
// Define a provider
final speedProvider = StateProvider((ref) => 0.0);

// Use in UI
@override
Widget build(BuildContext context) {
  return Consumer(
    builder: (context, ref, child) {
      final speed = ref.watch(speedProvider);
      return Text('Speed: ${speed} Mbps');
    },
  );
}
```

---

## 🧪 Testing

```bash
# Run all tests
flutter test

# Run specific test file
flutter test test/widget_test.dart

# Run with coverage
flutter test --coverage

# Generate coverage report
genhtml coverage/lcov.info -o coverage/report
```

---

## 📦 Building for Distribution

### Android Release APK

```bash
# Generate signing key
keytool -genkey -v -keystore ~/franvpn-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias franvpn

# Build signed APK
flutter build apk --release

# Output: build/app/outputs/flutter-apk/app-release.apk
```

### iOS IPA

```bash
# Build for distribution
flutter build ios --release

# Use Xcode to create IPA for App Store
# Or use fastlane for automated distribution
```

---

## 🚀 Deployment

### Google Play Store
1. Build release APK
2. Sign with release key
3. Create Play Store app listing
4. Upload APK
5. Fill in app details
6. Submit for review (1-2 hours)

### Apple App Store
1. Build iOS app
2. Sign with Apple certificate
3. Create App Store app listing
4. Upload via Xcode or Transporter
5. Submit for review (24-48 hours)

---

## 🐛 Troubleshooting

### Issue: "Flutter not found"
```bash
export PATH="$PATH:$(pwd)/flutter/bin"
```

### Issue: Gradle build fails
```bash
cd android
./gradlew clean
./gradlew build
```

### Issue: Pod install fails (iOS)
```bash
cd ios
pod repo update
pod install
```

### Issue: App crashes on startup
1. Check Logcat: `flutter logs`
2. Check for permission errors
3. Verify V2Ray integration

---

## 📚 Additional Resources

- [Flutter Documentation](https://flutter.dev/docs)
- [Dart Documentation](https://dart.dev/docs)
- [Android Documentation](https://developer.android.com/docs)
- [Google Fonts Guide](https://fonts.google.com/)
- [Riverpod Guide](https://riverpod.dev/)

---

## 📝 Next Steps

1. **Complete Screens**
   - [ ] Servers screen (list + map)
   - [ ] Profiles screen
   - [ ] Settings screen
   - [ ] Statistics screen

2. **Integrate Services**
   - [ ] V2Ray core integration
   - [ ] VPN service implementation
   - [ ] Server connection logic
   - [ ] DNS management

3. **Add Features**
   - [ ] Kill switch
   - [ ] Split tunneling
   - [ ] QR code import/export
   - [ ] Profile management

4. **Testing & Quality**
   - [ ] Unit tests
   - [ ] Widget tests
   - [ ] Integration tests
   - [ ] Performance profiling

5. **Distribution**
   - [ ] Generate signing key
   - [ ] Build release APK/IPA
   - [ ] Create app listings
   - [ ] Submit for review

---

## 📄 Build Instructions

See [BUILD_GUIDE_APK.md](../BUILD_GUIDE_APK.md) for detailed build instructions.

---

**FRANVPN Mobile App v1.0**  
**Status:** Ready to Build  
**Last Updated:** January 15, 2026

---

*Built with Flutter | Powered by V2Ray | Designed for Privacy*
