/**
 * @file    SofaArFragment.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191\@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This is the AR View. This was liberally copied and then modified.
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
package com.google.ar.core.examples.kotlin.sofasogood.arFragments

import android.opengl.GLSurfaceView
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Config
import com.google.ar.core.examples.java.common.helpers.SnackbarHelper
import com.google.ar.core.examples.java.common.helpers.TapHelper
import com.google.ar.core.examples.kotlin.helloar.R
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.SofaSoGoodActivity
import javax.inject.Inject

/** Contains UI elements for Hello AR. */

class SofaArView @Inject constructor(
    private val fragment: SofaArFragment,
    val activity: SofaSoGoodActivity,
    private val mainViewModel: MainViewModel
) : DefaultLifecycleObserver {

    val root = View.inflate(activity, R.layout.activity_main, null)
    val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview)

    val session
        //get() = fragment.arCoreSessionHelper.session
        get() = mainViewModel.arCoreSessionHelper.session

    val snackbarHelper = SnackbarHelper()
    val tapHelper = TapHelper(activity).also { surfaceView.setOnTouchListener(it) }

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    /**
     * Shows a pop-up dialog on the first tap in HelloARRenderer, determining whether the user wants
     * to enable depth-based occlusion. The result of this dialog can be retrieved with
     * DepthSettings.useDepthForOcclusion().
     */
    fun showOcclusionDialogIfNeeded() {
        val session = session ?: return

        val depthSettings = fragment.depthSettings

        val isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
        if (!depthSettings.shouldShowDepthEnableDialog() || !isDepthSupported) {
            return // Don't need to show dialog.
        }

        // Asks the user whether they want to use depth-based occlusion.
        AlertDialog.Builder(activity)
            .setTitle(R.string.options_title_with_depth)
            .setMessage(R.string.depth_use_explanation)
            .setPositiveButton(R.string.button_text_enable_depth) { _, _ ->
                depthSettings.setUseDepthForOcclusion(true)
            }
            .setNegativeButton(R.string.button_text_disable_depth) { _, _ ->
                depthSettings.setUseDepthForOcclusion(false)
            }
            .show()
    }

}

