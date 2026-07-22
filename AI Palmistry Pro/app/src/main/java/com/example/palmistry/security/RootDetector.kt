package com.example.palmistry.security

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * RootDetector - Detects rooted devices and emulators.
 * If detected, the app should refuse to run sensitive operations.
 */
@Singleton
class RootDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Returns true if the device appears to be rooted or running on an emulator.
     */
    fun isThreatDetected(): Boolean = isRooted() || isEmulator()

    // -------------------------------------------------------------------------
    // Root Detection
    // -------------------------------------------------------------------------
    private fun isRooted(): Boolean {
        return checkSuBinary() || checkRootManagementApps() || checkDangerousProps()
    }

    private fun checkSuBinary(): Boolean {
        val suPaths = listOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/system/su", "/system/bin/.ext/.su", "/system/usr/we-need-root/su-backup",
            "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su"
        )
        return suPaths.any { File(it).exists() }
    }

    private fun checkRootManagementApps(): Boolean {
        val rootApps = listOf(
            "com.noshufou.android.su",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine",
            "com.topjohnwu.magisk"
        )
        val pm = context.packageManager
        return rootApps.any { pkg ->
            try { pm.getPackageInfo(pkg, 0); true } catch (e: Exception) { false }
        }
    }

    private fun checkDangerousProps(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("getprop", "ro.debuggable"))
            val output = process.inputStream.bufferedReader().readLine()
            output?.trim() == "1"
        } catch (e: Exception) {
            false
        }
    }

    // -------------------------------------------------------------------------
    // Emulator Detection
    // -------------------------------------------------------------------------
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.PRODUCT == "google_sdk")
    }
}
