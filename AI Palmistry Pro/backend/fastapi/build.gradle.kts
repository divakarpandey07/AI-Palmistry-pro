/* Top-level build file */
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        // Hilt Gradle plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Enable Gradle caching for faster builds
gradle.startParameter.isBuildCacheEnabled = true
