# ProGuard / R8 rules for AI Palmistry Pro
# ---------------------------------------------------------------

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-keepclasseswithmembers class * {
    @dagger.hilt.android.AndroidEntryPoint *;
}

# Keep Room entities and DAOs
-keep class com.example.palmistry.data.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }

# Keep Retrofit service interfaces
-keep interface com.example.palmistry.network.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Keep Gson serialization models
-keep class com.example.palmistry.data.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.support.** { *; }

# Keep MediaPipe
-keep class com.google.mediapipe.** { *; }

# Keep SQLCipher
-keep class net.sqlcipher.** { *; }

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# General Android rules
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
