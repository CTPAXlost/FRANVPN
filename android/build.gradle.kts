// Top-level build file
plugins {
    id 'com.android.application' version '8.1.0' apply false
    id 'com.android.library' version '8.1.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.0' apply false
    id 'org.jetbrains.kotlin.jvm' version '1.9.0' apply false
    id 'org.jetbrains.kotlin.plugin.serialization' version '1.9.0' apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
