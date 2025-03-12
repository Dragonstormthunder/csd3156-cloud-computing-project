/**
 * @file    Colors.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This where we navigate through the screens. Also contains all the data objects to
 *          serialize the routes.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.uiElements

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

import androidx.compose.ui.graphics.Color

const val ColorBrown: String = "SealBrown"
const val ColorLightPink: String = "FairyTale"
const val ColorPink: String = "CarnationPink"
const val ColorLightBlue: String = "UranianBlue"
const val ColorBlue: String = "LightSkyBlue"

/**
 * The color list.
 */
val colorList = mapOf(
    ColorBrown to Color(0xFF531C00),
    ColorLightPink to Color(0xFFFFC8DD),
    ColorPink to Color(0xFFFFAFCC),
    ColorLightBlue to Color(0xFFBDE0FE),
    ColorBlue to Color(0xFFA2D2FF)
)

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
