package com.jonesandjay123.geminiapistarter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizePicture(
    navController: NavController
) {
    // 管理語言選擇
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("EN") }
    val languages = listOf("EN", "中文")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Recognize Picture")
                },
                actions = {
                    Box {
                        Text(
                            text = selectedLanguage,
                            modifier = Modifier
                                .clickable { expanded = true }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            languages.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(text = language) },
                                    onClick = {
                                        selectedLanguage = language
                                        expanded = false
                                        // 根據選擇的語言更新 UI（如有需要）
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "This is the Recognize Picture screen.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigateUp() }) {
                Text(text = "Back to BakingScreen")
            }
        }
    }
}
