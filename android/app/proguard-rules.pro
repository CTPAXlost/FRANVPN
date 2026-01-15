# Retrofit & OkHttp
-keep class com.franvpn.app.data.model.** { *; }
-keepclassmembers class com.franvpn.app.data.model.** { *; }
-keep interface com.franvpn.app.data.api.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Serialization
-keepclassmembers class * {
    *** *;
}
-keep @kotlinx.serialization.Serializable class * { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <fields>;
}

# VPN Service
-keep class android.net.VpnService { *; }
-keep class android.net.VpnService$Builder { *; }

# Bouncycastle
-keep class org.bouncycastle.** { *; }
-keepclassmembers class org.bouncycastle.** { *; }

# Androidx Security
-keep class androidx.security.crypto.** { *; }
-keepclassmembers class androidx.security.crypto.** { *; }

# Kotlin
-keepclassmembers class kotlin.Metadata {
    *** *;
}

# General
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# App Entry Points
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
