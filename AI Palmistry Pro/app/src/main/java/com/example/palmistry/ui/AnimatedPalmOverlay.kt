package com.example.palmistry.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

/**
 * AnimatedPalmOverlay – REMOVED scan guide lines per user request.
 * Now only shows the corner-bracket scan frame (handled in CameraScreen directly).
 * This file is kept as an empty composable for backward compatibility.
 */
@Composable
fun AnimatedPalmOverlay(modifier: Modifier = Modifier) {
    // Intentionally empty – scan lines removed per user request.
    // Palm line drawing is now done post-capture in CameraScreen.
}
