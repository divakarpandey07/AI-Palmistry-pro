# Implementation Plan

## Fix Gradle jlink path issue and stabilize build

**Goal**: Resolve intermittent `jlink executable ... does not exist` errors during `gradle assembleDebug` by ensuring the correct JDK path is configured for the Android build environment on Windows, both locally and in GitHub Actions CI.

### User Review Required

> [!IMPORTANT]
> Verify the absolute path to the JDK you want to use for building the Android app. The current configuration points to:
> `C:\\Users\\hp\\.antigravity\\extensions\\redhat.java-1.54.0-win32-x64\\jre\\21.0.10-win32-x86_64`
> If this path is incorrect or the JDK is missing, provide the correct JDK installation location.

### Open Questions

- Which JDK version should be used for the Android project? (e.g., JDK 17, JDK 21) 
- Do you prefer using the bundled JDK in the Antigravity extension or a system‑wide JDK installed at `C:\\Program Files\\Java`?
- Should the CI workflow also install the JDK automatically, or rely on a pre‑installed version?

### Proposed Changes

---
#### Gradle Configuration

- **[MODIFY] `gradle.properties`** (`C:/Users/hp/.gemini/antigravity/brain/3b82dffa-6b42-4284-8c07-5fc52a9797d7/AI Palmistry Pro/gradle.properties`)
  - Add or update `org.gradle.java.home` to point to a valid JDK directory.
  - Provide fallback logic using an environment variable `JAVA_HOME` if set.

- **[MODIFY] GitHub Actions workflow `android_build.yml`** (`C:/Users/hp/.gemini/antigravity/brain/3b82dffa-6b42-4284-8c07-5fc52a9797d7/AI Palmistry Pro/.github/workflows/android_build.yml`)
  - Install the required JDK version using `actions/setup-java` before running Gradle.
  - Ensure the `JAVA_HOME` environment variable is exported for the Gradle step.

---
#### Grahan / Transit Alert System (Feature Implementation)

- **[NEW] `GrahanAlertEngine.kt`** (`app/src/main/java/com/example/palmistry/feature/GrahanAlertEngine.kt`)
  - Core logic to load Grahan and planetary transit data from the local vector store (the three classical books).
  - Provides a function `getUpcomingTransits(): List<Transit>` that returns transits within the next 7 days.

- **[MODIFY] `HomeScreen.kt`** (`C:/Users/hp/.gemini/antigravity/brain/3b82dffa-6b42-4284-8c07-5fc52a9797d7/AI Palmistry Pro/app/src/main/java/com/example/palmistry/ui/HomeScreen.kt`)
  - Add a new composable card "Grahan & Transit Alerts" that displays the next significant transit (e.g., Solar Eclipse, Lunar Eclipse) with date and brief effect description.
  - Tap on the card navigates to a detailed screen.

- **[NEW] `TransitAlertScreen.kt`** (`app/src/main/java/com/example/palmistry/ui/TransitAlertScreen.kt`)
  - Shows a list of upcoming transits, each with an icon, date, and actionable advice.

- **[MODIFY] `NavGraph.kt`** (`C:/Users/hp/.gemini/antigravity/brain/3b82dffa-6b42-4284-8c07-5fc52a9797d7/AI Palmistry Pro/app/src/main/java/com/example/palmistry/ui/navigation/NavGraph.kt`)
  - Add navigation route for the new `TransitAlertScreen`.

- **[MODIFY] `RagEngine.kt`** (backend fastapi, if needed)
  - Add a query template for Grahan/Transit data retrieval.

---
### Verification Plan

#### Automated Tests
- Run `gradlew assembleDebug` locally after applying Gradle changes to ensure the `jlink` error is resolved.
- Execute the GitHub Actions workflow (trigger a manual workflow dispatch) and confirm the build completes without JDK errors.
- Add a unit test for `GrahanAlertEngine.getUpcomingTransits()` covering the next‑7‑day window.

#### Manual Verification
- Launch the app on an emulator/device and verify the new "Grahan & Transit Alerts" card appears on the home screen.
- Tap the card to view detailed transit listings and confirm the displayed information matches the vector‑store data.
- Check that the app still functions for existing features (hand scan, line overlay, etc.).

---
**Next Steps**
1. Await user confirmation on the JDK path and version preferences.
2. Upon approval, apply the Gradle configuration changes and implement the Grahan/Transit feature as outlined.
3. Run verification steps.
