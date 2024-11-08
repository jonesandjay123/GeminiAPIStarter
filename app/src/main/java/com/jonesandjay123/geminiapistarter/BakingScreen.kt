package com.jonesandjay123.geminiapistarter

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

val images = arrayOf(
    // Image generated using Gemini from the prompt "cupcake image"
    R.drawable.baked_goods_1,
    // Image generated using Gemini from the prompt "cookies images"
    R.drawable.baked_goods_2,
    // Image generated using Gemini from the prompt "cake images"
    R.drawable.baked_goods_3,
)
val imageDescriptions = arrayOf(
    R.string.image1_description,
    R.string.image2_description,
    R.string.image3_description,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakingScreen(
    navController: NavController, // 新增的參數
    bakingViewModel: BakingViewModel = viewModel()
) {
    // 管理語言選擇
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("EN") }
    val languages = listOf("EN", "中文")

    val selectedImage = remember { mutableIntStateOf(0) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 用於顯示本地選中的圖片
    var selectedImageUri by rememberSaveable { mutableStateOf<android.net.Uri?>(null) }

    // 圖片選擇器
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        selectedImageUri = uri
        // 根據是否選擇了圖片來更改 prompt
        prompt = if (uri != null) "請辨識圖片中的內容，並用繁體中文回答。" else placeholderPrompt
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.baking_title))
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
                .padding(innerPadding)
        ) {
            // 導航按鈕
            Button(
                onClick = { navController.navigate(Routes.RECOGNIZE_PICTURE) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text(text = "Go to RecognizePicture")
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(images) { index, image ->
                    var imageModifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .requiredSize(200.dp)
                        .clickable {
                            selectedImage.intValue = index
                        }
                    if (index == selectedImage.intValue) {
                        imageModifier =
                            imageModifier.border(BorderStroke(4.dp, MaterialTheme.colorScheme.primary))
                    }
                    Image(
                        painter = painterResource(image),
                        contentDescription = stringResource(imageDescriptions[index]),
                        modifier = imageModifier
                    )
                }
            }

            Row(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                TextField(
                    value = prompt,
                    label = { Text(stringResource(R.string.label_prompt)) },
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                )

                Button(
                    onClick = {
                        val bitmap = if (selectedImageUri != null) {
                            // 從上傳的圖片加載
                            val inputStream = context.contentResolver.openInputStream(selectedImageUri!!)
                            BitmapFactory.decodeStream(inputStream)
                        } else {
                            // 使用選定的內建圖片
                            BitmapFactory.decodeResource(
                                context.resources,
                                images[selectedImage.intValue]
                            )
                        }
                        bakingViewModel.sendPrompt(bitmap, prompt)
                    },
                    enabled = prompt.isNotEmpty(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = stringResource(R.string.action_go))
                }
            }

            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(text = "Choose Image from Gallery")
            }

            // 顯示選中的圖片以及移除按鈕
            selectedImageUri?.let { uri ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredSize(200.dp)
                            .padding(16.dp)
                    )

                    Button(
                        onClick = {
                            selectedImageUri = null
                            prompt = placeholderPrompt // 移除圖片後重置 prompt
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(text = "Remove Image")
                    }
                }
            }

            if (uiState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                var textColor = MaterialTheme.colorScheme.onSurface
                if (uiState is UiState.Error) {
                    textColor = MaterialTheme.colorScheme.error
                    result = (uiState as UiState.Error).errorMessage
                } else if (uiState is UiState.Success) {
                    textColor = MaterialTheme.colorScheme.onSurface
                    result = (uiState as UiState.Success).outputText
                }
                val scrollState = rememberScrollState()
                Text(
                    text = result,
                    textAlign = TextAlign.Start,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                )
            }
        }
    }
}
