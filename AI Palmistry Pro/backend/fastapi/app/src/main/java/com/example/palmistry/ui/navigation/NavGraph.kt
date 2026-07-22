package com.example.palmistry.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.palmistry.ui.CameraScreen
import com.example.palmistry.ui.HomeScreen
import com.example.palmistry.ui.ReadingHistoryScreen
import com.example.palmistry.ui.ReadingResultScreen
import com.example.palmistry.ui.viewmodel.HistoryViewModel
import com.example.palmistry.ui.viewmodel.PalmistryViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object Routes {
    const val HOME    = "home"
    const val CAMERA  = "camera"
    const val RESULT  = "result/{reading}"
    const val HISTORY = "history"

    fun resultRoute(reading: String): String = "result/${java.net.URLEncoder.encode(reading, StandardCharsets.UTF_8.toString())}"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.HOME,
    palmViewModel: PalmistryViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    var selectedLanguage by remember { mutableStateOf("Bilingual") }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Home Landing Dashboard Screen
        composable(Routes.HOME) {
            HomeScreen(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it },
                onStartScan = { navController.navigate(Routes.CAMERA) },
                onOpenHistory = { navController.navigate(Routes.HISTORY) }
            )
        }

        // Camera Scanner Screen with Flashlight & Capture
        composable(Routes.CAMERA) {
            CameraScreen(
                selectedLanguage = selectedLanguage,
                onPalmMetadataReady = { palmJson ->
                    palmViewModel.generateReading(palmJson)
                },
                onNavigateHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // Result screen with decoded reading text
        composable(
            route = Routes.RESULT,
            arguments = listOf(navArgument("reading") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("reading") ?: ""
            val reading = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            ReadingResultScreen(
                readingResult = reading,
                onBackToHome = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } }
            )
        }

        // History screen
        composable(Routes.HISTORY) {
            val readings by historyViewModel.readings.collectAsState(initial = emptyList())
            ReadingHistoryScreen(
                readings = readings,
                onDelete = { id -> historyViewModel.deleteReading(id) }
            )
        }
    }
}
