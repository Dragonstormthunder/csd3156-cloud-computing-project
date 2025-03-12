/**
 * @file    MainScreen.kt
 * @author  Lee Cjeng, Jacob
 * @par     Email: 2202750@gmail.com
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This is the main screen.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.screens

/* imports
------------------------------------------------------------------------------------------------- */

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.ar.core.examples.kotlin.helloar.R
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorBlue
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorBrown
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorLightBlue
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorLightPink
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorPink
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.QRButton
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.colorList

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// main screen
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Displays the main scene.
 *
 * @param   onClickToObjectBrowser
 *          When a button has been clicked, navigate to the object browser.
 *
 * @param   modifier
 *          The modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onClickToObjectBrowser: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = colorList[ColorBlue]!!
                ),
                title = {
                    Text("SofaSoGood", fontSize = 24.sp, color = colorList[ColorBrown]!!)
                }
            )
        }
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .background(colorList[ColorLightBlue]!!)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            //val mainViewModel: MainViewModel = hiltViewModel()

            //////////////////
            // chair icon

            Image(
                painter = painterResource(R.drawable.sofasogoodicon),
                contentDescription = "Icon of a Chair",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(5f, fill = false)
                    .aspectRatio(1f)
                    .border(
                        width = 10.dp,
                        color = colorList[ColorBrown]!!,
                        shape = CircleShape,
                    )
                    .clip(CircleShape)
                    .fillMaxWidth()
            )

            //////////////////
            // put space between the icon and the buttons

            Spacer(modifier = Modifier.weight(0.5f))

            //////////////////
            // object browser button

            var obExpanded by remember { mutableStateOf(false) }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colorList[ColorLightPink]!!,
                onClick = { obExpanded = !obExpanded },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = obExpanded,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150, 150)) togetherWith
                                fadeOut(animationSpec = tween(150)) using
                                SizeTransform { initialSize, targetSize ->
                                    if (targetState) {
                                        keyframes {
                                            // Expand horizontally first.
                                            IntSize(targetSize.width, initialSize.height) at 150
                                            durationMillis = 300
                                        }
                                    } else {
                                        keyframes {
                                            // Shrink vertically first.
                                            IntSize(initialSize.width, targetSize.height) at 150
                                            durationMillis = 300
                                        }
                                    }
                                }
                    },
                    label = "size transform",
                    modifier = Modifier.weight(9f)
                ) { targetExpanded ->
                    if (targetExpanded) {
                        OBExpanded(onClickToObjectBrowser)
                    } else {
                        OBIconContent(onClickToObjectBrowser)
                    }
                }

            }

            //////////////////
            // put space between the  buttons

            Spacer(modifier = Modifier.weight(0.2f))

            //////////////////
            // qr button

            var qrExpanded by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colorList[ColorLightPink]!!,
                onClick = { qrExpanded = !qrExpanded },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = qrExpanded,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150, 150)) togetherWith
                                fadeOut(animationSpec = tween(150)) using
                                SizeTransform { initialSize, targetSize ->
                                    if (targetState) {
                                        keyframes {
                                            // Expand horizontally first.
                                            IntSize(targetSize.width, initialSize.height) at 150
                                            durationMillis = 300
                                        }
                                    } else {
                                        keyframes {
                                            // Shrink vertically first.
                                            IntSize(initialSize.width, targetSize.height) at 150
                                            durationMillis = 300
                                        }
                                    }
                                }
                    },
                    label = "size transform",
                    //modifier = Modifier.weight(9f)
                ) { targetExpanded ->
                    QRContentView(targetExpanded)
                }
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * Previews the main scene.
 */
@Composable
@Preview(showSystemUi = true, showBackground = true, name = "MainScreen")
fun PreviewMainScene() {
    MainScreen()
}

/* ---------------------------------------------------------------------------------------------- */
/**
 * The QR content view.
 *
 * @param   targetExpanded
 *          Whether the content view is expanded or not.
 *
 * @param   modifier
 *          The modifier.
 */
@Composable
fun QRContentView(
    targetExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    var scannedText by remember { mutableStateOf("Scan a QR code") }
    val composeView = LocalView.current
    val viewModel = composeView.findViewTreeViewModelStoreOwner()?.let {
        hiltViewModel<MainViewModel>(it)
    }

    // the operation to carry out when the qr code scan is complete
    val scannedOperationComplete: (String) -> Unit = { result ->
        // execute operation here
        scannedText = result

        println("Scanned QR Code value: $result")

        if (viewModel != null) {
            viewModel.setQrCode(result)
        }
    }

    if (targetExpanded) {
        QRExpanded(
            modifier = modifier, // forward the modifier to the composable
            onScanComplete = scannedOperationComplete
        )
    } else {
        QRIconContent(
            modifier = modifier, // forward the modifier to the composable
            onScanComplete = scannedOperationComplete
        )
    }
    //viewModel.deleteAll()
    if (viewModel != null) {
        addBaseIntoDatabase(viewModel)
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * The QR Icon button.
 *
 * @param   qrIcon
 *          The image.
 *
 * @param   onScanComplete
 *          What to do when the QR scan has been completed.
 */
@Composable
private fun QRIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes qrIcon: Int,
    onScanComplete: (String) -> Unit
) {

    QRButton(
        modifier = modifier, //  passed to the lambda
        onQRCodeScanned = onScanComplete
        // this lambda defines the look of the QR Button
    ) { modifier2, onClick ->    // the modifier that was passed to the QRButton.
        // on click is the functionality that the QR button needs to execute
        // on clickable inside this composable view. if you have no such
        // elements that are clickable, you can ignore this parameter - the
        // QR button will make the entire thing clickable.

        IconButton(
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(qrIcon),
                contentDescription = "",
                tint = colorList[ColorBrown]!!,
                modifier = modifier2
            )
        }
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * The expanded QR Button's look.
 *
 * @param   modifier
 *          The modifier.
 *
 * @param   onScanComplete
 *          What to do when the QR scan has been completed.
 */
@Composable
fun QRExpanded(
    modifier: Modifier = Modifier,
    onScanComplete: (String) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "QR Scanner",
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
                    .offset(x = 36.dp),
                textAlign = TextAlign.Center,
                color = colorList[ColorBrown]!!
            )

            Spacer(modifier = Modifier.width(8.dp))

            QRIconButton(
                modifier = Modifier.background(colorList[ColorPink]!!),
                qrIcon = R.drawable.arrow_right,
                onScanComplete = onScanComplete
            )
        }

        Text(
            text = "Unlock new furniture by scanning QR Codes!",
            modifier = Modifier.padding(8.dp),
            color = colorList[ColorBrown]!!
        )

    }

}

/* ---------------------------------------------------------------------------------------------- */

/**
 * The mini QR Button's look.
 *
 * @param   modifier
 *          The modifier.
 *
 * @param   onScanComplete
 *          What to do when the QR scan has been completed.
 */
@Composable
fun QRIconContent(
    modifier: Modifier = Modifier,
    onScanComplete: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "QR Scanner",
            fontSize = 24.sp,
            modifier = Modifier
                .weight(1f)
                .offset(x = 36.dp),
            textAlign = TextAlign.Center,
            color = colorList[ColorBrown]!!
        )
        Spacer(modifier = Modifier.width(8.dp))

        QRIconButton(
            modifier = Modifier.background(colorList[ColorPink]!!),
            qrIcon = R.drawable.qr_icon,
            onScanComplete = onScanComplete
        )

    }

}

/* ---------------------------------------------------------------------------------------------- */

/**
 * The expanded object browser button's look.
 * @param onClickToObjectBrowser
 *        the scene that the button will redirect to
 */
@Composable
fun OBExpanded(onClickToObjectBrowser: () -> Unit = {}) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Object Browser",
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
                    .offset(x = 36.dp),
                textAlign = TextAlign.Center,
                color = colorList[ColorBrown]!!
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onClickToObjectBrowser
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "",
                    tint = colorList[ColorBrown]!!,
                    modifier = Modifier.background(colorList[ColorPink]!!)
                )
            }
        }
        Text(
            text = "Design your dream office from our in-app catalog!",
            modifier = Modifier.padding(8.dp),
            color = colorList[ColorBrown]!!
        )
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * The mini object browser button's look.
 * @param onClickToObjectBrowser
 *        the scene that the button will redirect to
 */
@Composable
fun OBIconContent(onClickToObjectBrowser: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Object Browser",
            fontSize = 24.sp,
            modifier = Modifier
                .weight(1f)
                .offset(x = 36.dp),
            textAlign = TextAlign.Center,
            color = colorList[ColorBrown]!!
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onClickToObjectBrowser
        ) {
            Icon(
                painter = painterResource(R.drawable.shopping_cart),
                contentDescription = "",
                tint = colorList[ColorBrown]!!,
                modifier = Modifier
                    .background(colorList[ColorPink]!!)
            )
        }
    }

}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */