#!/bin/bash

################################################################################
# FRANVPN Automatic APK Builder - Triggers on SDK Availability
################################################################################
#
# This script monitors for Android SDK availability and automatically builds
# APK files when the SDK becomes available.
#
# Usage:
#   chmod +x auto-build-when-sdk-ready.sh
#   ./auto-build-when-sdk-ready.sh
#
# Or run in background:
#   nohup ./auto-build-when-sdk-ready.sh > build.log 2>&1 &
#
################################################################################

set -e

# Configuration
PROJECT_DIR="/workspaces/FRANVPN"
BUILD_DIR="$PROJECT_DIR/android"
ANDROID_SDK_CHECK_INTERVAL=30  # Check every 30 seconds
MAX_WAIT_TIME=3600  # Give up after 1 hour
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

################################################################################
# Functions
################################################################################

log_info() {
    echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')] ✅ $1${NC}"
}

log_error() {
    echo -e "${RED}[$(date '+%Y-%m-%d %H:%M:%S')] ❌ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}[$(date '+%Y-%m-%d %H:%M:%S')] ⚠️  $1${NC}"
}

check_android_sdk() {
    if which android >/dev/null 2>&1; then
        return 0
    elif which sdkmanager >/dev/null 2>&1; then
        return 0
    elif [ -d "$ANDROID_HOME" ]; then
        return 0
    else
        return 1
    fi
}

build_apk() {
    log_info "Building FRANVPN APK files..."
    
    cd "$BUILD_DIR"
    
    # Check prerequisites
    log_info "Checking prerequisites..."
    if ! which java >/dev/null 2>&1; then
        log_error "Java not found. Please install Java 11+"
        return 1
    fi
    
    if ! which gradle >/dev/null 2>&1 && [ ! -f "./gradlew" ]; then
        log_error "Gradle wrapper not found"
        return 1
    fi
    
    # Make gradlew executable
    if [ -f "./gradlew" ]; then
        chmod +x ./gradlew
    fi
    
    log_info "Building Debug APK..."
    if ./gradlew clean assembleDebug 2>&1 | tee build-debug.log; then
        log_success "Debug APK built successfully"
    else
        log_error "Debug APK build failed"
        return 1
    fi
    
    log_info "Building Release APK..."
    if ./gradlew clean assembleRelease 2>&1 | tee build-release.log; then
        log_success "Release APK built successfully"
    else
        log_warning "Release APK build failed (may need signing keystore)"
    fi
    
    return 0
}

verify_apks() {
    log_info "Verifying APK files..."
    
    local debug_apk="$BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk"
    local release_apk="$BUILD_DIR/app/build/outputs/apk/release/app-release.apk"
    
    if [ -f "$debug_apk" ]; then
        local size=$(du -h "$debug_apk" | cut -f1)
        log_success "Debug APK: $debug_apk ($size)"
    else
        log_error "Debug APK not found"
        return 1
    fi
    
    if [ -f "$release_apk" ]; then
        local size=$(du -h "$release_apk" | cut -f1)
        log_success "Release APK: $release_apk ($size)"
    else
        log_warning "Release APK not found (may need keystore)"
    fi
    
    return 0
}

create_download_links() {
    log_info "Creating download information..."
    
    local download_file="$PROJECT_DIR/APK_DOWNLOAD_READY.txt"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    
    cat > "$download_file" << EOF
================================================================================
                         FRANVPN APK BUILD COMPLETE
================================================================================

Build Time: $timestamp
Status: ✅ READY

================================================================================
BUILT APK FILES
================================================================================

Debug APK (For Testing):
  Location: $BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk
  Size: $(du -h "$BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk" 2>/dev/null | cut -f1 || echo "Unknown")
  Use for: Development, testing, debugging

Release APK (For Distribution):
  Location: $BUILD_DIR/app/build/outputs/apk/release/app-release.apk
  Size: $(du -h "$BUILD_DIR/app/build/outputs/apk/release/app-release.apk" 2>/dev/null | cut -f1 || echo "Unknown (may need signing)")
  Use for: Distribution, production deployment

================================================================================
INSTALLATION METHODS
================================================================================

Method 1: Using ADB
  $ adb install $BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk

Method 2: Copy to Phone
  1. Copy APK file to phone storage
  2. Use file manager to navigate to APK
  3. Tap to install
  4. Grant permissions when prompted

Method 3: Create GitHub Release
  $ git tag v1.0.0
  $ git push origin v1.0.0
  → Creates permanent download link at:
    https://github.com/CTPAXlost/FRANVPN/releases

================================================================================
NEXT STEPS
================================================================================

1. Test APK:
   adb install $BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk
   adb shell am start -n com.franvpn.app/.ui.MainActivity

2. Commit to Git:
   cd $PROJECT_DIR
   git add .
   git commit -m "build: Auto-built APK with Android SDK"
   git push origin main

3. Create Release:
   git tag v1.0.0
   git push origin v1.0.0

4. Share:
   GitHub: https://github.com/CTPAXlost/FRANVPN/releases
   Direct: See links above

================================================================================
BUILD LOGS
================================================================================

Debug Build Log:
  $BUILD_DIR/build-debug.log

Release Build Log:
  $BUILD_DIR/build-release.log

Full Gradle Output:
  Check terminal output above

================================================================================
SUPPORT
================================================================================

For issues:
  1. Check build logs
  2. See DOWNLOAD_APK.md
  3. See APK_BUILD_GUIDE.md
  4. Visit: https://github.com/CTPAXlost/FRANVPN/issues

================================================================================

Build completed successfully! APK files are ready for download and installation.

$timestamp
EOF
    
    log_success "Download information saved to: $download_file"
}

monitor_and_build() {
    log_info "Starting APK auto-builder..."
    log_info "Monitoring for Android SDK availability (timeout: ${MAX_WAIT_TIME}s)..."
    
    local elapsed=0
    local check_interval=$ANDROID_SDK_CHECK_INTERVAL
    
    while [ $elapsed -lt $MAX_WAIT_TIME ]; do
        if check_android_sdk; then
            log_success "Android SDK detected!"
            
            if build_apk; then
                if verify_apks; then
                    create_download_links
                    log_success "=============================================="
                    log_success "FRANVPN APK BUILD COMPLETE"
                    log_success "=============================================="
                    log_success "Debug APK: $BUILD_DIR/app/build/outputs/apk/debug/app-debug.apk"
                    log_success "Release APK: $BUILD_DIR/app/build/outputs/apk/release/app-release.apk"
                    log_success "See APK_DOWNLOAD_READY.txt for details"
                    return 0
                else
                    log_error "APK verification failed"
                    return 1
                fi
            else
                log_error "APK build failed"
                return 1
            fi
        fi
        
        elapsed=$((elapsed + check_interval))
        log_info "Android SDK not found. Retrying in ${check_interval}s... ($elapsed/${MAX_WAIT_TIME}s)"
        sleep $check_interval
    done
    
    log_error "Timeout: Android SDK not available after ${MAX_WAIT_TIME} seconds"
    return 1
}

################################################################################
# Main
################################################################################

main() {
    clear
    echo "================================================================================"
    echo "              FRANVPN AUTOMATIC APK BUILDER"
    echo "================================================================================"
    echo
    log_info "Project directory: $PROJECT_DIR"
    log_info "Build directory: $BUILD_DIR"
    log_info "Start time: $TIMESTAMP"
    echo
    
    if [ ! -d "$BUILD_DIR" ]; then
        log_error "Build directory not found: $BUILD_DIR"
        exit 1
    fi
    
    if [ ! -f "$BUILD_DIR/gradlew" ]; then
        log_error "Gradle wrapper not found in $BUILD_DIR"
        exit 1
    fi
    
    monitor_and_build
    exit_code=$?
    
    echo
    echo "================================================================================"
    log_info "Build process completed (exit code: $exit_code)"
    echo "================================================================================"
    
    exit $exit_code
}

# Run main function
main "$@"
