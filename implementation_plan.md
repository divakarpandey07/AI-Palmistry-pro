# AI Palmistry Pro – Implementation Plan

## Goal
Create a highly secure, privacy‑first Android application **AI Palmistry Pro** that captures a hand image, extracts palm lines and mounts on‑device, encrypts the resulting metadata with AES‑256‑GCM, and sends it to a FastAPI backend. The backend uses a Supabase pgvector store and a quantized Llama‑3 8B model to perform Retrieval‑Augmented Generation (RAG) over three proprietary palmistry books, returning a personalized reading.

---

## User Review Required
> **[IMPORTANT]** This plan introduces several new components:
> - Android module with Jetpack Compose, CameraX, MediaPipe, TensorFlow Lite, Hilt, Room‑SQLCipher, and network security config.
> - FastAPI backend with Supabase pgvector, LangChain, and Llama‑3 8B (quantized).
> - End‑to‑end encryption (AES‑256‑GCM) and TLS 1.3 with certificate pinning.
> Ensure you are comfortable with these technology choices before proceeding.

---

## Open Questions
> **[INFO]** All open questions have been answered in the previous messages. No further clarification needed at this stage.

---

## Proposed Changes
### Android Frontend
- **[NEW]** `app` directory with Gradle Kotlin DSL (`build.gradle.kts`) including dependencies:
  - `androidx.camera:camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`
  - `com.google.mediapipe:mediapipe-hands`
  - `org.tensorflow:tensorflow-lite`
  - `com.google.dagger:hilt-android` and `hilt-compiler`
  - `androidx.room:room-runtime` with `room-sqlcipher`
  - `com.squareup.retrofit2:retrofit` and `converter-moshi`
  - `androidx.security:security-crypto` (for AES‑GCM)
- **[NEW]** Hilt setup (`Application` class, modules).
- **[NEW]** CameraX preview composable using `AndroidView`.
- **[NEW]** `EncryptionUtil.kt` implementing AES‑256‑GCM.
- **[NEW]** Network security config XML enforcing TLS 1.3 and certificate pinning.
- **[NEW]** Repository layer (Retrofit service) for `/read` endpoint.

### Backend Service
- **[NEW]** FastAPI project (`main.py`, `router.py`).
- **[NEW]** Supabase client for pgvector (SQLAlchemy + `vector` extension).
- **[NEW]** LangChain `SupabaseVectorStore` wrapper.
- **[NEW]** RAG chain: retrieve relevant chunks → Llama‑3 inference → prompt guardrails.
- **[NEW]** Endpoint `/read` accepting encrypted JSON, decrypting, processing, and returning encrypted response.
- **[NEW]** Dockerfile for containerised deployment.

### Security
- **[NEW]** AES‑256‑GCM utility on both client and server.
- **[NEW]** TLS 1.3 only network config with pinned SHA‑256 certificate hash.
- **[NEW]** Root/emulator detection utility in Android app.
- **[NEW]** Rate‑limiting middleware in FastAPI (using `slowapi`).

### DevOps / CI
- **[NEW]** GitHub Actions workflow for Android build, unit tests, and lint.
- **[NEW]** GitHub Actions workflow for backend Docker image build and push.

---

## Verification Plan
### Automated Tests
- Android unit tests for `EncryptionUtil` and Retrofit service mocks.
- FastAPI integration tests covering decryption, vector retrieval, and LLM response validation.
- CI pipelines execute the above on each PR.

### Manual Verification
- Capture a hand image, ensure on‑device model returns a non‑empty JSON metadata.
- Verify encrypted payload reaches backend and is decrypted correctly.
- Confirm the reading references the three source books (spot‑check extracted text).
- Test root/emulator detection blocks API calls.
- Perform a prompt‑injection attempt and verify the guardrails reject the request.

Once you approve this plan, I will scaffold the Android project and set up the initial `build.gradle.kts` with the listed dependencies.
