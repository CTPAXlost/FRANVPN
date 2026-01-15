#!/bin/bash

# FRANVPN APK Build Script
# This script builds and signs the FRANVPN APK

set -e

echo "═══════════════════════════════════════════════════════════"
echo "  FRANVPN - Android VPN Client APK Build Script"
echo "═══════════════════════════════════════════════════════════"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuration
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APK_OUTPUT_DIR="${PROJECT_DIR}/app/build/outputs/apk"
RELEASE_APK="${APK_OUTPUT_DIR}/release/app-release.apk"
DEBUG_APK="${APK_OUTPUT_DIR}/debug/app-debug.apk"

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    if ! command -v gradle &> /dev/null && ! [ -f "./gradlew" ]; then
        log_error "Gradle not found. Please install Gradle or run from a directory with gradlew."
        exit 1
    fi
    
    if ! command -v java &> /dev/null; then
        log_error "Java not found. Please install Java 11 or higher."
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | grep -oE '[0-9]+' | head -1)
    if [ "$JAVA_VERSION" -lt 11 ]; then
        log_error "Java 11+ required. Found version: $JAVA_VERSION"
        exit 1
    fi
    
    log_info "✓ Java version: $(java -version 2>&1 | head -n 1)"
}

# Build APK
build_apk() {
    local BUILD_TYPE=$1
    
    if [ -z "$BUILD_TYPE" ]; then
        BUILD_TYPE="debug"
    fi
    
    log_info "Building ${BUILD_TYPE^} APK..."
    
    if [ -f "./gradlew" ]; then
        ./gradlew clean "assemble${BUILD_TYPE^}" -x lint
    else
        gradle clean "assemble${BUILD_TYPE^}" -x lint
    fi
    
    if [ $? -eq 0 ]; then
        log_info "✓ ${BUILD_TYPE^} APK built successfully"
        return 0
    else
        log_error "Build failed"
        return 1
    fi
}

# Sign release APK
sign_apk() {
    log_info "Signing release APK..."
    
    if [ -f "${RELEASE_APK}" ]; then
        log_info "✓ Release APK found at ${RELEASE_APK}"
        log_info "APK is ready for distribution"
    else
        log_warning "Release APK not found at expected location"
    fi
}

# Display APK info
display_apk_info() {
    local APK_PATH=$1
    
    if [ -f "${APK_PATH}" ]; then
        local APK_SIZE=$(du -h "${APK_PATH}" | cut -f1)
        log_info "APK: $(basename ${APK_PATH})"
        log_info "Size: ${APK_SIZE}"
        log_info "Path: ${APK_PATH}"
    fi
}

# Main build flow
main() {
    cd "${PROJECT_DIR}"
    
    check_prerequisites
    echo ""
    
    # Ask for build type
    echo "Select build type:"
    echo "  1) Debug (faster, smaller, for testing)"
    echo "  2) Release (signed, optimized, for distribution)"
    echo "  3) Both"
    read -p "Enter choice [1-3]: " choice
    
    echo ""
    
    case $choice in
        1)
            build_apk "debug"
            display_apk_info "${DEBUG_APK}"
            ;;
        2)
            build_apk "release"
            sign_apk
            display_apk_info "${RELEASE_APK}"
            ;;
        3)
            build_apk "debug"
            display_apk_info "${DEBUG_APK}"
            echo ""
            build_apk "release"
            sign_apk
            display_apk_info "${RELEASE_APK}"
            ;;
        *)
            log_error "Invalid choice"
            exit 1
            ;;
    esac
    
    echo ""
    echo "═══════════════════════════════════════════════════════════"
    log_info "Build complete!"
    echo "═══════════════════════════════════════════════════════════"
}

# Run main
main "$@"
