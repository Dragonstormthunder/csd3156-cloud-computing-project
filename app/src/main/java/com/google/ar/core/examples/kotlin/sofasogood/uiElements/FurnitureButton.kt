/**
 * @file    FurnitureButton.kt
 * @author  @Lim Li Hui, Trina
 * @par     Email
 *     lihuitrina.lim@digipen.edu
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   This is the furniture item button.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.uiElements

/* imports
------------------------------------------------------------------------------------------------- */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.ModelInformation
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.listModelInformation

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
////////////////////////////////////////////////////////////////////////////////////////////////////
// furniture item
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Displays the object list of furniture
 *
 * @param   modelInformation
 *          The model information of this specific item.
 *
 * @param   viewModel
 *          The view model for sharing data.
 */
@Composable
fun FurnitureButton(
    modelInformation: ModelInformation,
    viewModel: MainViewModel = hiltViewModel()
) {

    val currentlySelectedInformation = viewModel.currentlySelectedInformation.collectAsState()

    Card(
        onClick = {
            // people will start tapping immediately to place it down, set it back to true
            viewModel.updateAllowUserTappingToPlace(true)

            // set the settings for the displays
            viewModel.updateDisplayObjectBrowserOverlay(false)
            viewModel.updateCurrentlySelectedInformation(
                currentlySelectedInformation.value.copy(
                    modelIndex = modelInformation.index
                )
            )

        },
        modifier = Modifier
            .padding(10.dp)
            .size(100.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(colorList[ColorPink]!!)
        ) {

            Image(
                painter = painterResource(id = modelInformation.displayPath),
                contentDescription = modelInformation.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize()
            )

            Text(
                text = modelInformation.name,
                color = colorList[ColorBrown]!!,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }
}

/* ---------------------------------------------------------------------------------------------- */

/**
 * Previews the furniture item.
 */
@Composable
@Preview(showSystemUi = true, showBackground = true, name = "FurnitureItem")
fun PreviewFurnitureItem() {
    FurnitureButton(listModelInformation[0])
}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */