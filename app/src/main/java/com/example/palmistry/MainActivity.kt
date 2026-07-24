package com.example.palmistry

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.palmistry.security.RootDetector
import com.example.palmistry.ui.MainApp
import com.example.palmistry.ui.theme.PalmistryTheme
import com.example.palmistry.ui.viewmodel.PalmistryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var rootDetector: RootDetector
    private val viewModel: PalmistryViewModel by viewModels()

    // Request both CAMERA and flashlight-related permissions
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val cameraGranted = results[Manifest.permission.CAMERA] ?: false
        if (!cameraGranted) {
            Toast.makeText(this, "Camera permission zaruri hai!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔒 Security check – block on rooted devices / emulators
        if (rootDetector.isThreatDetected()) {
            Toast.makeText(this, "Security threat detected. App cannot run.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Request camera + flashlight permissions
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                // Flashlight uses the camera hardware – no separate permission needed on Android 13+
            )
        )

        setContent {
            PalmistryTheme {
                val uiState by viewModel.uiState.collectAsState()
                MainApp(
                    uiState = uiState,
                    onPalmCaptured = { palmJson -> viewModel.generateReading(palmJson) },
                    onReset = { viewModel.resetState() }
                )
            }
        }
    }
}
