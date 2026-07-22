# Task List – AI Palmistry Pro (Android Foundation)

1. **Initialize Project Structure**
   - Create folder `AI_Palmistry_Pro/` with standard Android Gradle layout:
     - `settings.gradle.kts`
     - `build.gradle.kts` (project level)
     - `app/` module containing:
       - `src/main/kotlin/com/example/palmistry/`
       - `src/main/res/`
       - `src/main/AndroidManifest.xml`
       - `build.gradle.kts` (module level)
   - Add `gradle/wrapper/` (Gradle Wrapper) if not present.

2. **Configure Project‑Level build.gradle.kts**
   - Set `repositories { google(); mavenCentral() }`.
   - Define Kotlin version, Android Gradle Plugin version, and enable KSP plugin.

3. **Configure Module‑Level build.gradle.kts** (app)
   - Apply plugins: `com.android.application`, `org.jetbrains.kotlin.android`, `dagger.hilt.android.plugin`, `kotlin-kapt`, `com.google.devtools.ksp`.
   - Set `compileSdk`, `minSdk`, `targetSdk`.
   - Add **Compose BOM** and all required dependencies (CameraX, ARCore, MediaPipe, TensorFlow Lite, Security‑Crypto, SQLCipher, Hilt, Retrofit, OkHttp) using the exact versions from the architecture description.
   - Enable `buildFeatures { compose true }` and Kotlin compiler extension version.
   - Configure `packagingOptions` to exclude duplicate META‑INF files.
   - Add ProGuard/R8 rules for MediaPipe, TFLite, and Hilt.

4. **Create AndroidManifest.xml**
   - Permissions: `CAMERA`, `INTERNET`, `ACCESS_NETWORK_STATE`, `WRITE_EXTERNAL_STORAGE` (if needed), `READ_EXTERNAL_STORAGE` (for loading assets).
   - Add ARCore meta‑data:
     ```xml
     <meta-data android:name="com.google.ar.core" android:value="required" />
     ```
   - Set `android:usesCleartextTraffic="false"` (enforced by network security config).
   - Add `android:hardwareAccelerated="true"`.

5. **Add Network Security Config** (`res/xml/network_security_config.xml`)
   - Enforce TLS 1.3 only, disable clear‑text.
   - Include certificate pinning placeholders.

6. **Create Hilt Application Class**
   - `class PalmistryApp : Application() { @EntryPoint override fun onCreate() { super.onCreate() } }` with `@HiltAndroidApp` annotation.

7. **Create AdaptiveScannerManager.kt**
   - Place in `src/main/kotlin/com/example/palmistry/scanner/`.
   - Use the code provided in the user message (copy verbatim, adjusting package name if needed).

8. **Create FingerAnalyzer.kt** and related data classes.
   - Location: `src/main/kotlin/com/example/palmistry/ml/`.
   - Use the user's `FingerAnalysis`, `FingerAnalyzer` implementation.

9. **Create HandLineProcessor.kt**
   - Location: `src/main/kotlin/com/example/palmistry/ml/`.
   - Use the user‑provided implementation (including TFLite loading, JSON conversion).
   - Add asset placeholder for `palm_analyzer.tflite`.

10. **Create EncryptionUtil.kt**
    - Provide AES‑256‑GCM helpers using `androidx.security:security-crypto`.

11. **Create Retrofit API Service**
    - `interface ApiService { @POST("/rag") suspend fun getReading(@Body encryptedPayload: RequestBody): Response<ReadingResponse> }`
    - Configure OkHttp client with certificate pinning and HMAC signing.

12. **Create Room Database with SQLCipher**
    - `ReadingEntity.kt`, `ReadingDao.kt`, `AppDatabase.kt` under `db/` package.
    - Use `net.zetetic:android-database-sqlcipher` and `androidx.security:security-crypto` for the passphrase.

13. **Add Compose UI Stubs**
    - `MainActivity.kt` with NavHost (CameraScreen → ResultScreen → HistoryScreen).
    - `CameraScreen.kt` showing `AndroidView` wrapping `PreviewView`.
    - `ResultScreen.kt` displaying reading text.

14. **Add ProGuard / R8 Rules**
    - Create `proguard-rules.pro` with keep rules for MediaPipe, TensorFlow Lite, Hilt, Retrofit, and any generated model classes.

15. **Sync Gradle & Verify**
    - Run `./gradlew clean assembleDebug` (once Android CLI is installed) to ensure the project builds.
    - Fix any compile errors, especially missing KSP/Kapt plugins.

---

**Next Immediate Action**
- Create the project skeleton (folders, `settings.gradle.kts`, top‑level `build.gradle.kts`).
- Add the module‑level `build.gradle.kts` with the dependency block from the architecture.
- Generate `AndroidManifest.xml` with required permissions.
- Add the three core Kotlin files: `AdaptiveScannerManager.kt`, `FingerAnalyzer.kt`, `HandLineProcessor.kt`.

After these files are in place, we can run a Gradle sync and confirm the baseline builds before proceeding to camera preview integration and ML inference.
