/**
 * @file    QRButtonLogic.kt
 * @author  Guo Chen
 * @par     Email: 2200518\@sit.singaporetech.edu.sg
 * @author  Emma Natalie Soh
 * @par     Email: 2202191\@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    22 February 2025
 *
 * @brief   This where we navigate through the screens. Also contains all the data objects to
 *          serialize the routes.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.uiElements

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.integration.android.IntentIntegrator

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

/**
 * The QR Null result.
 * */
const val qrNullResult: String = "no_result"

/**
 * A QR Code button.
 *
 * @param   modifier
 *          The modifier to be applied on the composable.
 *
 * @param   onQRCodeScanned
 *          When a QR Code is scanned, this is the value that's returned.
 *
 * @param   content
 *          The appearance of the button.
 *          Just FYI, you do not need to use the second parameter in your lambda functions, i.e. the
 *          () -> Unit [for the composable]. It's only required if you have a clickable in your
 *          composable.
 *
 *
 * Example of usage:
val qrResult = remember { mutableStateOf("") }
QRButton(
onQRCodeScanned = { scanned ->
qrResult.value = scanned
}
) { modifier, onClick ->
Button(
onClick = onClick,
modifier
) {
Text("QR Code")
}
}
Text("QR Result: ${qrResult.value}")
 */
@Composable
fun QRButton(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit = {},
    content: @Composable (Modifier, () -> Unit) -> Unit // UI as a parameter
) {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // remember
    ////////////////////////////////////////////////////////////////////////////////////////////////

    val qrResult = remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // qr code logic
    ////////////////////////////////////////////////////////////////////////////////////////////////

    val qrLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            qrResult.value = intentResult.contents ?: qrNullResult
            onQRCodeScanned(qrResult.value)
        }
    }

    val onClick: () -> Unit = {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val integrator = IntentIntegrator(activity)
        integrator.setOrientationLocked(true)
        qrLauncher.launch(integrator.createScanIntent())
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // clickable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // content is the composable look that they gave this button
    content(modifier.clickable { onClick() }, onClick)
}

/* ---------------------------------------------------------------------------------------------- */

// old code
//@Composable
//private fun QRButton(
//    viewModel: MainViewModel = viewModel(),
//    modifier: Modifier = Modifier
//) {
//
//    val qrResult = remember { mutableStateOf("") }
//    val context = LocalContext.current
//    val activity = context as? Activity
//    val qrLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
//            qrResult.value = intentResult.contents ?: "No result"
//        }
//    }
//
//    Button(
//        onClick = {
//
//        }
//    ) {
//        Text("QR Code")
//    }
//
//    Text("QR Result: ${qrResult.value}")
//
//}

/* ---------------------------------------------------------------------------------------------- */
