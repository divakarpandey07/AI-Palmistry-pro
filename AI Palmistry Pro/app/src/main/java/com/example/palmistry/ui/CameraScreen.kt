package com.example.palmistry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CameraScreen(
    navController: NavHostController,
    onPalmMetadataReady: (String) -> Unit
) {
    // Simple placeholder UI representing camera preview
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Camera Preview Placeholder",
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Flashlight toggle button – opens a dialog
            var showDialog by remember { mutableStateOf(false) }
            OutlinedButton(onClick = { showDialog = true }) {
                Text("Flashlight")
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Home navigation button
            OutlinedButton(onClick = { navController.navigate(com.example.palmistry.ui.navigation.Routes.HOME) }) {
                Text("Home")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Flashlight") },
            text = { Text("Flashlight toggle would be here (placeholder).") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
