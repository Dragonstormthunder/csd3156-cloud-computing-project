/**
 * @file    SofaArFragment.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191\@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This is the AR fragment. This was liberally copied and then modified from
 *          HelloArActivity to modify it from an activity to a fragment.
 *
 * Copyright from Google:
 *
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* package
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.arFragments

/* imports
------------------------------------------------------------------------------------------------- */

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.ar.core.examples.java.common.helpers.CameraPermissionHelper
import com.google.ar.core.examples.java.common.helpers.DepthSettings
import com.google.ar.core.examples.java.common.helpers.InstantPlacementSettings
import com.google.ar.core.examples.java.common.samplerender.SampleRender
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.SofaSoGoodActivity

/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// ar fragment
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore API. The application will display any detected planes and will allow the user to tap on a
 * plane to place a 3D model.
 */
class SofaArFragment : Fragment() {

    companion object {
        const val TAG = "SofaArFragment"
    }

    lateinit var sofaArView: SofaArView
    private lateinit var sofaArRenderer: SofaArRenderer

    // added this - Ezra Emma
    private val mainViewModel: MainViewModel by activityViewModels()

    val instantPlacementSettings = InstantPlacementSettings()
    val depthSettings = DepthSettings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize any AR feature settings.
        instantPlacementSettings.onCreate(requireActivity())
        depthSettings.onCreate(requireActivity())

        // Initialize the ARCore session helper in the ViewModel.
        mainViewModel.initializeARCoreSessionHelper(requireActivity(), instantPlacementSettings)
        // Add the helper as an observer so it handles lifecycle events.

        lifecycle.addObserver(mainViewModel.arCoreSessionHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create our custom AR view.
        sofaArView = SofaArView(this, requireActivity() as SofaSoGoodActivity, mainViewModel)
        // Add it as a lifecycle observer.
        lifecycle.addObserver(sofaArView)
        return sofaArView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create and add the AR renderer.
        sofaArRenderer =
            SofaArRenderer(this, requireActivity() as SofaSoGoodActivity, mainViewModel)
        lifecycle.addObserver(sofaArRenderer)

        // Set up the renderer with our surface view, renderer, and assets.
        SampleRender(sofaArView.surfaceView, sofaArRenderer, requireActivity().assets)
    }

    // Configure the session, using Lighting Estimation, and Depth mode.
//    fun configureSession(session: Session) {
//        session.configure(
//            session.config.apply {
//                lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
//
//                // Depth API is used if it is configured in Hello AR's settings.
//                depthMode =
//                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                        Config.DepthMode.AUTOMATIC
//                    } else {
//                        Config.DepthMode.DISABLED
//                    }
//
//                // Instant Placement is used if it is configured in Hello AR's settings.
//                instantPlacementMode =
//                    if (instantPlacementSettings.isInstantPlacementEnabled) {
//                        InstantPlacementMode.LOCAL_Y_UP
//                    } else {
//                        InstantPlacementMode.DISABLED
//                    }
//            }
//        )
//    }

    @Deprecated("Depreciated. Here to suppress warning")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        val sofaSoGoodActivity = (requireActivity() as SofaSoGoodActivity)

        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!CameraPermissionHelper.hasCameraPermission(sofaSoGoodActivity)) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(
                sofaSoGoodActivity,
                "Camera permission is needed to run this application", Toast.LENGTH_LONG
            ).show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(sofaSoGoodActivity)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(sofaSoGoodActivity)
            }

            // @todo maybe return to the previous screen?

            Log.e("SofaArFragment: Request Permissions", "Permissions not given")
            sofaSoGoodActivity.finish()
        }
    }

//    fun onWindowFocusChanged(hasFocus: Boolean) {
//        val sofaSoGoodActivity = (requireActivity() as SofaSoGoodActivity)
//        FullScreenHelper.setFullScreenOnWindowFocusChanged(sofaSoGoodActivity, hasFocus)
//    }

}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
