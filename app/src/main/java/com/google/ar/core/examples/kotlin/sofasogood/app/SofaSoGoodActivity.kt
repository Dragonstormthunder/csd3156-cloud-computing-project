/**
 * @file    SofaSoGoodActivity.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This file defines the activity.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.app

/* imports
------------------------------------------------------------------------------------------------- */

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.examples.kotlin.sofasogood.screens.SofaSoGoodNavigation
import dagger.hilt.android.AndroidEntryPoint

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// sofarsogood activity
////////////////////////////////////////////////////////////////////////////////////////////////////

//class SofaSoGoodActivity : ComponentActivity() {
@AndroidEntryPoint
class SofaSoGoodActivity : AppCompatActivity() {

    //private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            // navigation controller
            val navController = rememberNavController()

            // main view model & injection
//            val viewModelFactory = MainViewModel = hiltViewModel()

            SofaSoGoodNavigation(
                navHostController = navController,
                modifier = Modifier//.padding(innerPadding)
            )
        }
    }
}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
