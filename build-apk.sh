#!/bin/bash
set -e

cd android

# Ensure gradlew is executable
chmod +x gradlew

# Download gradle distribution and extract it to use gradle command
if ! command -v gradle &> /dev/null; then
    echo "Downloading Gradle 8.1.1..."
    mkdir -p ~/.gradle/wrapper/dists
    
    curl -L -o /tmp/gradle-8.1.1-bin.zip "https://services.gradle.org/distributions/gradle-8.1.1-bin.zip"
    unzip -q /tmp/gradle-8.1.1-bin.zip -d ~/.gradle/wrapper/dists/
    
    export PATH="$HOME/.gradle/wrapper/dists/gradle-8.1.1/bin:$PATH"
fi

# Build APK
echo "Building Debug APK..."
gradle clean assembleDebug

echo "✅ Build complete!"
ls -lh app/build/outputs/apk/debug/app-debug.apk
