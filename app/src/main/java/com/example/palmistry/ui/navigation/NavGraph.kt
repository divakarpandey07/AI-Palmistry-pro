package com.example.palmistry.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.palmistry.ui.ReadingHistoryScreen
import com.example.palmistry.ui.viewmodel.HistoryViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Routes {
    const val CAMERA  = "camera"
    const val RESULT  = "result/{reading}"
    const val HISTORY = "history"
    const val HOME    = "home"

    fun resultRoute(reading: String): String =
        "result/${URLEncoder.encode(reading, StandardCharsets.UTF_8.toString())}"
}

/**
 * NavGraph is only used for the HISTORY sub-screen now.
 * Main navigation is handled by MainApp's internal AppScreen state machine.
 */
@Composable
fun HistoryNavHost(onBack: () -> Unit) {
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val readings by historyViewModel.readings.collectAsState()
    ReadingHistoryScreen(
        readings = readings,
        onDelete = { id -> historyViewModel.deleteReading(id) },
        onBack = onBack
    )
}
