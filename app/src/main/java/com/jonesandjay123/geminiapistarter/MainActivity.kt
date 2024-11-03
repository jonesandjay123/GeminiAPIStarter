package com.jonesandjay123.geminiapistarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jonesandjay123.geminiapistarter.ui.theme.GeminiAPIStarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiAPIStarterTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.BAKING_SCREEN) {
        composable(Routes.BAKING_SCREEN) {
            BakingScreen(navController = navController)
        }
        composable(Routes.RECOGNIZE_PICTURE) {
            RecognizePicture(navController = navController)
        }
    }
}