/**
 * @file    QrScannerScene.kt
 * @author  Kenzie Lim
 * @par     Email: 2200709@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    22 February 2025
 *
 * @brief   The QR scanner composable for this application.
 */

package com.google.ar.core.examples.kotlin.sofasogood.screens

import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.repo.SofaSoGoodEntity

//@Composable
//fun QRSceneScreen(viewModel: MainViewModel = hiltViewModel()) {
//    var scannedText by remember { mutableStateOf("Scan a QR code") }
//    val card by viewModel.card.collectAsState()
//    val reposList by viewModel.allSofas.collectAsState()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        QRCodeScanner { result ->
//            scannedText = result
//            viewModel.setQrCode(result)
//        }
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//        ) {
//            Text(
//                text = scannedText,
//                color = Color.White,
//                modifier = Modifier.padding(8.dp)
//            )
//
//            if (card?.isUnlocked == true) {
//                UnlockedCard(card!!)
//            }
//        }
//    }
//    viewModel.deleteAll()
//    addBaseIntoDatabase(viewModel)
//}

/*
*  @PrimaryKey val qrCode : String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "isUnlocked") val isUnlocked: Boolean = false,*/

fun addBaseIntoDatabase(viewModel: MainViewModel) {
    viewModel.insert(SofaSoGoodEntity("Cube", "Cube", "Its a Cube!", 0, true))
    viewModel.insert(SofaSoGoodEntity("Chair", "Chair", "Its a chair!", 1, false))
    viewModel.insert(SofaSoGoodEntity("Sofa", "Sofa", "Its a sofa!", 2, false))
    viewModel.insert(SofaSoGoodEntity("Bed", "Bed", "Its a Bed!", 3, false))
    viewModel.insert(SofaSoGoodEntity("Table", "Table", "Its a table!", 4, false))
}

//@Composable
//fun UnlockedCard(card: SofaSoGoodEntity) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = card.title, style = MaterialTheme.typography.headlineSmall)
//            Text(text = card.description, style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}

//@Composable
//fun QRCodeScanner(onScanResult: (String) -> Unit) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    var hasCameraPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val cameraPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { granted ->
//            hasCameraPermission = granted
//        }
//    )
//
//    LaunchedEffect(key1 = true) {
//        if (!hasCameraPermission) {
//            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    if (hasCameraPermission) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//            var barcodeScanner by remember { mutableStateOf<BarcodeScanner?>(null) }
//
//            AndroidView(
//                factory = { ctx ->
//                    val previewView = PreviewView(ctx)
//                    val executor = Executors.newSingleThreadExecutor()
//
//                    cameraProviderFuture.addListener({
//                        val cameraProvider = cameraProviderFuture.get()
//                        val preview = Preview.Builder().build().also {
//                            it.setSurfaceProvider(previewView.surfaceProvider)
//                        }
//
//                        val imageAnalysis = ImageAnalysis.Builder()
//                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                            .build()
//                            .also {
//                                it.setAnalyzer(executor) { imageProxy ->
//                                    processImageProxy(imageProxy, barcodeScanner) { barcode ->
//                                        onScanResult(barcode)
//                                    }
//                                }
//                            }
//
//                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//                        try {
//                            cameraProvider.unbindAll()
//                            cameraProvider.bindToLifecycle(
//                                lifecycleOwner,
//                                cameraSelector,
//                                preview,
//                                imageAnalysis
//                            )
//                        } catch (e: Exception) {
//                            Log.e("QRCodeScanner", "Camera binding failed", e)
//                        }
//                    }, ContextCompat.getMainExecutor(ctx))
//
//                    previewView
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//
//            LaunchedEffect(key1 = Unit) {
//                barcodeScanner = BarcodeScanning.getClient()
//            }
//        }
//    } else {
//        // Handle permission denied case
//    }
//}

//@androidx.camera.core.ExperimentalGetImage
//private fun processImageProxy(
//    imageProxy: androidx.camera.core.ImageProxy,
//    barcodeScanner: BarcodeScanner?,
//    onScanResult: (String) -> Unit
//) {
//    barcodeScanner?.let { scanner ->
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
//            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//            scanner.process(image)
//                .addOnSuccessListener { barcodes ->
//                    for (barcode in barcodes) {
//                        when (barcode.valueType) {
//                            Barcode.TYPE_TEXT -> {
//                                barcode.rawValue?.let { onScanResult(it) }
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.e("QRCodeScanner", "Barcode scanning failed", e)
//                }
//                .addOnCompleteListener {
//                    imageProxy.close()
//                }
//        } else {
//            imageProxy.close()
//        }
//    }
//}