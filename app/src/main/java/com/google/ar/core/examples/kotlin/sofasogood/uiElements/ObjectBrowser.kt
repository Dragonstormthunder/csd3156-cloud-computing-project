/**
 * @file    ObjectBrowser.kt
 * @author  @Lim Li Hui, Trina
 * @par     Email
 *     lihuitrina.lim@digipen.edu
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   This is object browser screen.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.uiElements

/* imports
------------------------------------------------------------------------------------------------- */

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ar.core.examples.kotlin.helloar.R
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.listModelInformation

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// object browser screen
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Displays the object browser scene.
 *
 * @param   viewModel
 *          The view model for sharing data.
 *
 * @param   modifier
 *          The modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectBrowser(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = colorList[ColorBlue]!!
                ),
                title = {

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // close button
                        IconButton(
                            onClick = {
                                //viewModel.displayObjectBrowserOverlay.value = false
                                viewModel.updateDisplayObjectBrowserOverlay(false)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "Close Catalog",
                                tint = colorList[ColorBrown]!!,
                                modifier = modifier
                            )
                        }

                        // put some space
                        Spacer(modifier = Modifier.weight(0.5f))

                        // title
                        Text(
                            "Catalog",
                            fontSize = 24.sp,
                            color = colorList[ColorBrown]!!,
                            modifier = Modifier.weight(8f)
                        )

                        // qr button
                        QRIconButton(modifier = Modifier.weight(1f))

                        // put some space at the end
                        Spacer(modifier = Modifier.weight(0.5f))

                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorList[ColorLightBlue]!!)
        ) {

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 5.dp,
                color = colorList[ColorBlue]!!
            )

            ObjectList(viewModel = viewModel)

            //QRButton() //not sure what the params are supposed to be
        }

    }

}

/* ---------------------------------------------------------------------------------------------- */

/**
 * Previews the object browser.
 */
//@Composable
//@Preview(showSystemUi = true, showBackground = true, name = "ObjectBrowserScreen")
//fun PreviewObjectBrowser() {
//    ObjectBrowser()
//}

/* ---------------------------------------------------------------------------------------------- */

/**
 * Displays the object list of furniture
 *
 * @param   viewModel
 *          The view model for sharing data.
 */
@Composable
fun ObjectList(
    viewModel: MainViewModel = hiltViewModel()
) {
    val card by viewModel.card.collectAsState()
    Log.d("Composable", "Card: $card")
    val sofas by viewModel.allSofas.collectAsState()
    //lazy grid with cards that show furniture
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {

        items(sofas) { sofa ->
            Log.d("Composable sofa", "Model: $sofa")
//            if (model.displayPath != 0) {
//                if(card != null) {
//                    Log.d("FurnitureButton", "Card: ${card}, Model: ${model}")
            if (sofa.qrCode == listModelInformation[sofa.modelID].name) {
                if (sofa.isUnlocked) {
                    FurnitureButton(
                        modelInformation = listModelInformation[sofa.modelID],
                        viewModel = viewModel
                    )
                }
//                    }
//                }
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * This icon displays the QR Button.
 */
@Composable
fun QRIconButton(
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = hiltViewModel()
    // the operation to carry out when the qr code scan is complete
    val scannedOperationComplete: (String) -> Unit = { result ->
        // execute operation here
        println("Scanned QR Code value: $result")
        viewModel.setQrCode(result)
    }

    QRButton(
        modifier = modifier, //  passed to the lambda
        onQRCodeScanned = scannedOperationComplete
        // this lambda defines the look of the QR Button
    ) { modifier2, onClick ->    // the modifier that was passed to the QRButton.
        // on click is the functionality that the QR button needs to execute
        // on clickable inside this composable view. if you have no such
        // elements that are clickable, you can ignore this parameter - the
        // QR button will make the entire thing clickable.

        IconButton(
            onClick = onClick,
            modifier = modifier2
        ) {
            Icon(
                painter = painterResource(R.drawable.qr_icon),
                contentDescription = "",
                tint = colorList[ColorBrown]!!,
                modifier = modifier//.background(colorList.get(ColorPink)!!)
            )
        }
    }

}

/* ---------------------------------------------------------------------------------------------- */