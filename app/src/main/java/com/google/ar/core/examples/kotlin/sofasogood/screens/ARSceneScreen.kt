/**
 * @file    ARSceneScreen.kt
 * @author  @todo
 * @par     @todo
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This is the AR scene screen.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.screens

/* imports
------------------------------------------------------------------------------------------------- */

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.Vec3
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.listModelInformation
import com.google.ar.core.examples.kotlin.sofasogood.arFragments.SofaArFragment
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorBlue
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorBrown
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ColorPink
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.ObjectBrowser
import com.google.ar.core.examples.kotlin.sofasogood.uiElements.colorList

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// ar screen
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Displays the AR scene.
 *
 * @param   modifier
 *          The modifier.
 */
@Composable
fun ARSceneScreen(
    modifier: Modifier = Modifier
) {

    //val mainViewModel: MainViewModel = hiltViewModel()

    val fragment = SofaArFragment()

    // this box will help overlay the jetpack composable stuff on top of the ar scene
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        ////////////////////////////////////////////////////////////////////////////////////////////
        // the AR fragment
        ////////////////////////////////////////////////////////////////////////////////////////////

        val fragmentManager = (LocalContext.current as AppCompatActivity).supportFragmentManager

        // just a fixed ID so that it doesn't regenerate
        // nowhere else has it, should be OK
        val fragmentId = 12345

        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { context ->

                val frameLayout = FrameLayout(context).apply {
                    id = fragmentId
                }

                fragmentManager.beginTransaction()
                    .replace(frameLayout.id, fragment, SofaArFragment.TAG)
                    .commit()

                // cannot return in a layout.
                frameLayout
            }
        )

        ////////////////////////////////////////////////////////////////////////////////////////////
        // jetpack composable
        ////////////////////////////////////////////////////////////////////////////////////////////

        ARScreenUI(
            modifier = Modifier
                .matchParentSize()
                .padding(16.dp)
        )

    }

}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

/**
 * The UI for the AR Screen.
 *
 * @param   modifier
 *          The modifier.
 */
@Composable
private fun ARScreenUI(
    modifier: Modifier = Modifier
) {

    ////////////////////////////////////////////////////////////////////////////////////////////
    // collect as state stuff
    ////////////////////////////////////////////////////////////////////////////////////////////

    val composeView = LocalView.current
    val viewModel = composeView.findViewTreeViewModelStoreOwner()?.let {
        hiltViewModel<MainViewModel>(it)
    }
    if (viewModel == null) {
        return
    }

    val displayObjectBrowserOverlay by viewModel.displayObjectBrowserOverlay.collectAsState()
    val isTapPlace = viewModel.isAllowUserTappingToPlace.collectAsState()
    val anchorInformation = viewModel.latestAnchorInformation.collectAsState()
    val currentlySelectedInformation = viewModel.currentlySelectedInformation.collectAsState()
    val modelIndex = currentlySelectedInformation.value.modelIndex

    ////////////////////////////////////////////////////////////////////////////////////////////
    // ui stuff
    ////////////////////////////////////////////////////////////////////////////////////////////

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.absolutePadding(bottom = 20.dp)
    ) {

        ////////////////////////////////////////////////////////////////////////////////////////////
        // data values
        ////////////////////////////////////////////////////////////////////////////////////////////

        val minScale = 0.5f
        val maxScale = 10f
        val offsetDampening = 0.0001f // really need to dampen it, cuz offset uses dp

        ////////////////////////////////////////////////////////////////////////////////////////////
        // gesture zone
        ////////////////////////////////////////////////////////////////////////////////////////////

        val state = rememberTransformableState { zoomChange, offsetChange, _ ->

            viewModel.updateLatestAnchorInformation(
                anchorInformation.value.copy(
                    scale = anchorInformation.value.scale * zoomChange,
                    position = Vec3(
                        anchorInformation.value.position.x + offsetChange.x * offsetDampening,
                        anchorInformation.value.position.y,
                        anchorInformation.value.position.z + offsetChange.y * offsetDampening
                    )
                )
            )

            // do some scaling clamping
            if (anchorInformation.value.scale.x < minScale) {
                viewModel.updateLatestAnchorInformation(
                    anchorInformation.value.copy(
                        scale = Vec3(minScale, minScale, minScale)
                    )
                )
            }

            if (anchorInformation.value.scale.x > maxScale) {
                viewModel.updateLatestAnchorInformation(
                    anchorInformation.value.copy(
                        scale = Vec3(maxScale, maxScale, maxScale)
                    )
                )
            }

        }

        Box(
            modifier = Modifier
                .weight(9f)
                //.background(Color(0x55FFFFFF))
                .fillMaxSize()
                // only transformable if tap place not engaged
                .then(if (isTapPlace.value) Modifier else Modifier.transformable(state = state))
        ) {
            // empty. here to collect interactions only
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // changing objects + clearing objects row
        ////////////////////////////////////////////////////////////////////////////////////////////

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            val buttonPinkColors = ButtonColors(
                containerColor = colorList[ColorPink]!!,
                contentColor = colorList[ColorBrown]!!,
                // won't be disabled, anyway
                disabledContentColor = colorList[ColorPink]!!,
                disabledContainerColor = colorList[ColorBrown]!!
            )
            val buttonBlueColors = ButtonColors(
                containerColor = colorList[ColorBlue]!!,
                contentColor = colorList[ColorBrown]!!,
                // won't be disabled, anyway
                disabledContentColor = colorList[ColorBlue]!!,
                disabledContainerColor = colorList[ColorBrown]!!
            )

            // this button displays the model information name
            // and the value
            Button(
                onClick = {

                    //viewModel.displayObjectBrowserOverlay.value = true
                    viewModel.updateDisplayObjectBrowserOverlay(true)

                },
                colors = buttonPinkColors,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val modelInformation = listModelInformation[modelIndex]

                    Image(
                        painter = painterResource(modelInformation.displayPath),
                        contentDescription = "Image of ${modelInformation.name}",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize(0.15f)
                            .aspectRatio(1f)
                            .border(
                                width = 2.dp,
                                color = colorList[ColorBrown]!!,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    Text(
                        text = modelInformation.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }

            }

            Spacer(modifier = Modifier.weight(0.1f))

            // this button lets the user tap to do different modes
            Button(
                onClick = {
                    viewModel.updateAllowUserTappingToPlace(!isTapPlace.value)
                },
                colors = if (isTapPlace.value) buttonBlueColors else buttonPinkColors,
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxWidth()
            ) {
                Text(if (isTapPlace.value) "Place" else "Edit")
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////
        // rotational logic
        ////////////////////////////////////////////////////////////////////////////////////////

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            ////////////////////////////
            // the text

            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Rotation",
                    style = TextStyle(
                        color = colorList[ColorPink]!!,
                        drawStyle = Stroke(width = 3f),
                    ),
                )

                Text(
                    text = "Rotation",
                    color = colorList[ColorBrown]!!,
                )

            }

            Spacer(Modifier.weight(0.1f))

            ////////////////////////////
            // slider for rotation
            val fill = anchorInformation.value.rotation.y / 360f

            Slider(
                value = fill,
                onValueChange = {
                    viewModel.updateLatestAnchorInformation(
                        anchorInformation.value.copy(
                            rotation = Vec3(0f, it * 360f, 0f)
                        )
                    )
                },
                colors = SliderDefaults.colors(
                    thumbColor = colorList[ColorBrown]!!,
                    activeTrackColor = colorList[ColorPink]!!,
                    inactiveTrackColor = colorList[ColorBlue]!!,
                ),
                //steps = 3,
                // normalized range
                valueRange = 0f..1f,
                modifier = Modifier.weight(8f)
            )
        }

    }

    if (displayObjectBrowserOverlay) {
        ObjectBrowser(viewModel)
    }

}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
