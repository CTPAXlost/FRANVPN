#!/bin/bash

# FRANVPN APK Build Script
# This script automates the APK building process

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║         FRANVPN APK Builder (Flutter)              ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════╝${NC}"
echo ""

# Check if Flutter is installed
if ! command -v flutter &> /dev/null; then
    echo -e "${RED}❌ Flutter is not installed!${NC}"
    echo ""
    echo "Please install Flutter first:"
    echo "  1. Visit: https://flutter.dev/docs/get-started/install"
    echo "  2. Run: flutter doctor"
    echo "  3. Then run this script again"
    exit 1
fi

# Check Flutter version
echo -e "${YELLOW}Checking Flutter setup...${NC}"
flutter doctor

echo ""
echo -e "${YELLOW}Getting dependencies...${NC}"
flutter pub get

echo ""
echo -e "${YELLOW}Building options:${NC}"
echo "  1) Debug APK (fast, larger ~50-80MB)"
echo "  2) Release APK (optimized, ~30-40MB)"
echo "  3) Release Split APKs (smallest, one per device type)"
echo ""

# Default to release if no argument provided
BUILD_TYPE=${1:-2}

case $BUILD_TYPE in
  1)
    echo -e "${YELLOW}Building Debug APK...${NC}"
    flutter build apk --debug
    APK_PATH="build/app/outputs/flutter-apk/app-debug.apk"
    ;;
  2)
    echo -e "${YELLOW}Building Release APK...${NC}"
    flutter build apk --release
    APK_PATH="build/app/outputs/flutter-apk/app-release.apk"
    ;;
  3)
    echo -e "${YELLOW}Building Split APKs (optimized for distribution)...${NC}"
    flutter build apk --split-per-abi --release
    APK_PATH="build/app/outputs/flutter-apk/"
    ;;
  *)
    echo -e "${RED}Invalid option!${NC}"
    exit 1
    ;;
esac

# Check if build was successful
if [ -f "$APK_PATH" ] || [ -d "$APK_PATH" ]; then
    echo ""
    echo -e "${GREEN}✅ Build successful!${NC}"
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo "APK location:"
    ls -lh "$APK_PATH"
    echo ""
    echo -e "${BLUE}Installation:${NC}"
    echo "  • Automatic: flutter install"
    echo "  • Manual: adb install $APK_PATH"
    echo ""
else
    echo -e "${RED}❌ Build failed!${NC}"
    exit 1
fi
