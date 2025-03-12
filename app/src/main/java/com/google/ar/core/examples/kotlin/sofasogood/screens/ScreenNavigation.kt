/**
 * @file    ScreenNavigation.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This where we navigate through the screens. Also contains all the data objects to
 *          serialize the routes.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.screens

/* imports
------------------------------------------------------------------------------------------------- */

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// navigation stuff
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @brief   Controls the navigation.
 *
 * @param   navHostController
 *          The navigation controller.
 *
 * @param   startDestination
 *          Where the navigation should begin.
 *
 * @param   modifier
 *          The modifier applied onto the screens as a whole.
 */
@Composable
fun SofaSoGoodNavigation(
    navHostController: NavHostController,
    startDestination: Any = MainRoute,
    modifier: Modifier = Modifier
) {
    NavHost(navHostController, startDestination = startDestination) {

        composable<ArSceneRoute> {
            ARSceneScreen(
                modifier = modifier
            )
        }

        composable<MainRoute> {
            MainScreen(
                onClickToObjectBrowser = {
                    navHostController.navigate(ArSceneRoute)
                },
                modifier = modifier
            )
        }
    }

}

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// navigation logic in different functions
////////////////////////////////////////////////////////////////////////////////////////////////////

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// screen serializable
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Defines the parameters required for the route to the AR Scene screen.
 */
@Serializable
object ArSceneRoute

//@Serializable
//object QRSceneRoute

/**
 * Defines the parameters required for the route to the main screen.
 */
@Serializable
object MainRoute

/* ---------------------------------------------------------------------------------------------- */