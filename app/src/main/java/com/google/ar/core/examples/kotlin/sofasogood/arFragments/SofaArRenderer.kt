/**
 * @file    ARSceneScreen.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191\@sit.singaporetech.edu.sg
 * @author  Goh Jun Lin Wayne 2200628
 * @par     Email: junlinwayne.goh\@digipen.edu
 *
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    16 February 2025
 *
 * @brief   This is the AR Renderer. This was liberally copied and then modified.
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

import android.opengl.GLES30
import android.opengl.Matrix
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Camera
import com.google.ar.core.DepthPoint
import com.google.ar.core.Frame
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.Plane
import com.google.ar.core.Point
import com.google.ar.core.Session
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper
import com.google.ar.core.examples.java.common.helpers.TrackingStateHelper
import com.google.ar.core.examples.java.common.samplerender.Framebuffer
import com.google.ar.core.examples.java.common.samplerender.GLError
import com.google.ar.core.examples.java.common.samplerender.Mesh
import com.google.ar.core.examples.java.common.samplerender.SampleRender
import com.google.ar.core.examples.java.common.samplerender.Shader
import com.google.ar.core.examples.java.common.samplerender.Texture
import com.google.ar.core.examples.java.common.samplerender.VertexBuffer
import com.google.ar.core.examples.java.common.samplerender.arcore.BackgroundRenderer
import com.google.ar.core.examples.java.common.samplerender.arcore.PlaneRenderer
import com.google.ar.core.examples.java.common.samplerender.arcore.SpecularCubemapFilter
import com.google.ar.core.examples.kotlin.helloar.R
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.Vec3
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.listModelInformation
import com.google.ar.core.examples.kotlin.sofasogood.app.MainViewModel
import com.google.ar.core.examples.kotlin.sofasogood.app.SofaSoGoodActivity
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.AnchorModificationInformation
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.WrappedAnchor
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.NotYetAvailableException
import java.io.IOException
import java.nio.ByteBuffer
import javax.inject.Inject


/* functions
------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */

////////////////////////////////////////////////////////////////////////////////////////////////////
// ar fragment
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @brief   Sofa Renderer Class
 *
 * @param   fragment
 *          The sofa Ar Fragment
 *
 * @param   activity
 *          The sofa activity
 *
 * @param   mainViewModel
 *          The main view model
 */
class SofaArRenderer @Inject constructor(
    private val fragment: SofaArFragment,
    val activity: SofaSoGoodActivity,
    private val mainViewModel: MainViewModel
) :

    SampleRender.Renderer, DefaultLifecycleObserver {

    companion object {

        const val TAG = "SofaArRenderer"

        // See the definition of updateSphericalHarmonicsCoefficients for an explanation of these
        // constants.
//        private val sphericalHarmonicFactors =
//            floatArrayOf(
//                0.282095f,
//                -0.325735f,
//                0.325735f,
//                -0.325735f,
//                0.273137f,
//                -0.273137f,
//                0.078848f,
//                -0.273137f,
//                0.136569f
//            )

        private const val Z_NEAR = 0.1f
        private const val Z_FAR = 100f

        // Assumed distance from the device camera to the surface on which user will try to place
        // objects.
        // This value affects the apparent scale of objects while the tracking method of the
        // Instant Placement point is SCREENSPACE_WITH_APPROXIMATE_DISTANCE.
        // Values in the [0.2, 2.0] meter range are a good choice for most AR experiences. Use lower
        // values for AR experiences where users are expected to place objects on surfaces close to the
        // camera. Use larger values for experiences where the user will likely be standing and trying
        // to
        // place an object on the ground or floor in front of them.
        const val APPROXIMATE_DISTANCE_METERS = 2.0f

        const val CUBEMAP_RESOLUTION = 16
        const val CUBEMAP_NUMBER_OF_IMPORTANCE_SAMPLES = 32
    }

    lateinit var render: SampleRender
    private lateinit var planeRenderer: PlaneRenderer
    private lateinit var backgroundRenderer: BackgroundRenderer
    private lateinit var virtualSceneFramebuffer: Framebuffer
    private var hasSetTextureNames = false

    // Point Cloud
    private lateinit var pointCloudVertexBuffer: VertexBuffer
    private lateinit var pointCloudMesh: Mesh
    private lateinit var pointCloudShader: Shader

    // Keep track of the last point cloud rendered to avoid updating the VBO if point cloud
    // was not changed.  Do this using the timestamp since we can't compare PointCloud objects.
    private var lastPointCloudTimestamp: Long = 0

    // Virtual object (ARCore pawn)
    private lateinit var virtualObjectShader: Shader

    // Environmental HDR
    private lateinit var dfgTexture: Texture
    private lateinit var cubemapFilter: SpecularCubemapFilter

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private var modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16) // view x model

    private val modelViewProjectionMatrix = FloatArray(16) // projection x view x model

    val session
        //get() = fragment.arCoreSessionHelper.session
        get() = mainViewModel.arCoreSessionHelper.session

    private val displayRotationHelper = DisplayRotationHelper(activity)
    private val trackingStateHelper = TrackingStateHelper(activity)

    private val wrappedAnchors = mutableListOf<WrappedAnchor>()


    override fun onResume(owner: LifecycleOwner) {
        displayRotationHelper.onResume()
        hasSetTextureNames = false
    }

    override fun onPause(owner: LifecycleOwner) {
        displayRotationHelper.onPause()
    }

    /**
    @brief
    Function that loads all models and textures
    @param render
    the current renderer to render all objects
     */
    private fun loadAllModels(render: SampleRender) {

        Log.d("LoadAllModels", "Load Start")

        for (i in listModelInformation) {

            Log.d("LoadAllModels", "Loading Model ${i.modelPath}")

            i.mesh = Mesh.createFromAsset(render, i.modelPath)
            i.texture = Texture.createFromAsset(
                render,
                i.texturePath,
                Texture.WrapMode.CLAMP_TO_EDGE,
                Texture.ColorFormat.SRGB
            )
        }

        Log.d("LoadAllModels", "Load End")

    }

    /**
    @brief
    Function that renders all models
    @param render
    the current renderer to render all objects
     */
    private fun renderAllModels(render: SampleRender) {

        if (wrappedAnchors.isNotEmpty()) {
            wrappedAnchors.last().scale = mainViewModel.latestAnchorInformation.value.scale
            wrappedAnchors.last().position = mainViewModel.latestAnchorInformation.value.position
            wrappedAnchors.last().rotation = mainViewModel.latestAnchorInformation.value.rotation
        }

        for ((anchor, _, model, scale, position, rotation) in
        wrappedAnchors.filter { it.anchor.trackingState == TrackingState.TRACKING }) {


            // Get the current pose of an Anchor in world space. The Anchor pose is updated
            // during calls to session.update() as ARCore refines its estimate of the world.
            anchor.pose.toMatrix(modelMatrix, 0)

            val scaleMatrix = FloatArray(16)
            Matrix.setIdentityM(scaleMatrix, 0)  // Set scaleMatrix to identity

            val translateMatrix = FloatArray(16)
            Matrix.setIdentityM(translateMatrix, 0)

            val rotationMatrix = FloatArray(16)
            Matrix.setIdentityM(rotationMatrix, 0)

            // Apply the scaling transformation
            val newScale = scale * listModelInformation[model].initialScale
            Matrix.scaleM(scaleMatrix, 0, newScale.x, newScale.y, newScale.z)

            // apply the translate
            Matrix.translateM(translateMatrix, 0, position.x, position.y, position.z)

            // apply the rotation
            Matrix.rotateM(rotationMatrix, 0, rotation.x, 1f, 0f, 0f)
            Matrix.rotateM(rotationMatrix, 0, rotation.y, 0f, 1f, 0f)
            Matrix.rotateM(rotationMatrix, 0, rotation.z, 0f, 0f, 1f)

            // Calculate model/view/projection matrices
            Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, translateMatrix, 0)
            Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, rotationMatrix, 0)
            Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scaleMatrix, 0)
            Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)

            /* Update shader properties and draw */
            //set model view projection matrix
            virtualObjectShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)

            //set texture matrix
            virtualObjectShader.setTexture("u_Texture", listModelInformation[model].texture)
            render.draw(
                listModelInformation[model].mesh,
                virtualObjectShader,
                virtualSceneFramebuffer
            )
        }
    }

    /**
    @brief
    Function that calls when the surface is created, this function is mean to render objects
    @param render
    the current renderer to render all objects
     */
    override fun onSurfaceCreated(render: SampleRender) {
        // Prepare the rendering objects.
        // This involves reading shaders and 3D model files, so may throw an IOException.
        try {
            planeRenderer = PlaneRenderer(render)
            backgroundRenderer = BackgroundRenderer(render)
            virtualSceneFramebuffer = Framebuffer(render, /*width=*/ 1, /*height=*/ 1)

            cubemapFilter =
                SpecularCubemapFilter(
                    render,
                    CUBEMAP_RESOLUTION,
                    CUBEMAP_NUMBER_OF_IMPORTANCE_SAMPLES
                )
            // Load environmental lighting values lookup table
            dfgTexture =
                Texture(
                    render,
                    Texture.Target.TEXTURE_2D,
                    Texture.WrapMode.CLAMP_TO_EDGE,
                    /*useMipmaps=*/ false
                )
            // The dfg.raw file is a raw half-float texture with two channels.
            val dfgResolution = 64
            val dfgChannels = 2
            val halfFloatSize = 2

            val buffer: ByteBuffer =
                ByteBuffer.allocateDirect(dfgResolution * dfgResolution * dfgChannels * halfFloatSize)
            activity.assets.open("models/dfg.raw").use { it.read(buffer.array()) }

            // SampleRender abstraction leaks here.
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, dfgTexture.textureId)
            GLError.maybeThrowGLException("Failed to bind DFG texture", "glBindTexture")
            GLES30.glTexImage2D(
                GLES30.GL_TEXTURE_2D,
                /*level=*/ 0,
                GLES30.GL_RG16F,
                /*width=*/ dfgResolution,
                /*height=*/ dfgResolution,
                /*border=*/ 0,
                GLES30.GL_RG,
                GLES30.GL_HALF_FLOAT,
                buffer
            )
            GLError.maybeThrowGLException("Failed to populate DFG texture", "glTexImage2D")

            // Point cloud
            pointCloudShader =
                Shader.createFromAssets(
                    render,
                    "shaders/point_cloud.vert",
                    "shaders/point_cloud.frag",
                    /*defines=*/ null
                )
                    .setVec4(
                        "u_Color",
                        floatArrayOf(31.0f / 255.0f, 188.0f / 255.0f, 210.0f / 255.0f, 1.0f)
                    )
                    .setFloat("u_PointSize", 5.0f)

            // four entries per vertex: X, Y, Z, confidence
            pointCloudVertexBuffer =
                VertexBuffer(render, /*numberOfEntriesPerVertex=*/ 4, /*entries=*/ null)
            val pointCloudVertexBuffers = arrayOf(pointCloudVertexBuffer)
            pointCloudMesh =
                Mesh(
                    render,
                    Mesh.PrimitiveMode.POINTS, /*indexBuffer=*/
                    null,
                    pointCloudVertexBuffers
                )

            loadAllModels(render)

            virtualObjectShader =
                Shader.createFromAssets(
                    render,
                    "shaders/ar_unlit_object.vert",
                    "shaders/ar_unlit_object.frag",
                    mapOf("NUMBER_OF_MIPMAP_LEVELS" to cubemapFilter.numberOfMipmapLevels.toString())
                )
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read a required asset file", e)
            showError("Failed to read a required asset file: $e")
        }
    }

    override fun onSurfaceChanged(render: SampleRender, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
        virtualSceneFramebuffer.resize(width, height)
    }

    override fun onDrawFrame(render: SampleRender) {
        val session = session ?: return
        val depthSettings = fragment.depthSettings

        // Texture names should only be set once on a GL thread unless they change. This is done during
        // onDrawFrame rather than onSurfaceCreated since the session is not guaranteed to have been
        // initialized during the execution of onSurfaceCreated.
        if (!hasSetTextureNames) {
            session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))
            hasSetTextureNames = true
        }

        // -- Update per-frame state

        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper.updateSessionIfNeeded(session)

        // Obtain the current frame from ARSession. When the configuration is set to
        // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
        // camera framerate.
        val frame =
            try {
                session.update()
            } catch (e: CameraNotAvailableException) {
                Log.e(TAG, "Camera not available during onDrawFrame", e)
                showError("Camera not available. Try restarting the app.")
                return
            }

        val camera = frame.camera

        // Update BackgroundRenderer state to match the depth settings.
        try {
            backgroundRenderer.setUseDepthVisualization(
                render,
                depthSettings.depthColorVisualizationEnabled()
            )
            backgroundRenderer.setUseOcclusion(
                render,
                depthSettings.useDepthForOcclusion()
            )
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read a required asset file", e)
            showError("Failed to read a required asset file: $e")
            return
        }

        // BackgroundRenderer.updateDisplayGeometry must be called every frame to update the coordinates
        // used to draw the background camera image.
        backgroundRenderer.updateDisplayGeometry(frame)
        val shouldGetDepthImage =
            depthSettings.useDepthForOcclusion() || depthSettings.depthColorVisualizationEnabled()
        if (camera.trackingState == TrackingState.TRACKING && shouldGetDepthImage) {
            try {
                val depthImage = frame.acquireDepthImage16Bits()
                backgroundRenderer.updateCameraDepthTexture(depthImage)
                depthImage.close()
            } catch (e: NotYetAvailableException) {
                // This normally means that depth data is not available yet. This is normal so we will not
                // spam the logcat with this.
            }
        }

        // Handle one tap per frame.
        handleTap(frame, camera)

        // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
        trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

        // Show a message based on whether tracking has failed, if planes are detected, and if the user
        // has placed any objects.
        val message: String? =
            when {
                camera.trackingState == TrackingState.PAUSED &&
                        camera.trackingFailureReason == TrackingFailureReason.NONE ->
                    fragment.getString(R.string.searching_planes)

                camera.trackingState == TrackingState.PAUSED ->
                    TrackingStateHelper.getTrackingFailureReasonString(camera)

                session.hasTrackingPlane() && wrappedAnchors.isEmpty() ->
                    fragment.getString(R.string.waiting_taps)

                session.hasTrackingPlane() && wrappedAnchors.isNotEmpty() -> null
                else -> fragment.getString(R.string.searching_planes)
            }
        if (message == null) {
            fragment.sofaArView.snackbarHelper.hide(activity)
        } else {
            fragment.sofaArView.snackbarHelper.showMessage(activity, message)
        }

        // -- Draw background
        if (frame.timestamp != 0L) {
            // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
            // drawing possible leftover data from previous sessions if the texture is reused.
            backgroundRenderer.drawBackground(render)
        }

        // If not tracking, don't draw 3D objects.
        if (camera.trackingState == TrackingState.PAUSED) {
            return
        }

        // -- Draw non-occluded virtual objects (planes, point cloud)

        // Get projection matrix.
        camera.getProjectionMatrix(projectionMatrix, 0, Z_NEAR, Z_FAR)

        // Get camera matrix and draw.
        camera.getViewMatrix(viewMatrix, 0)
        frame.acquirePointCloud().use { pointCloud ->
            if (pointCloud.timestamp > lastPointCloudTimestamp) {
                pointCloudVertexBuffer.set(pointCloud.points)
                lastPointCloudTimestamp = pointCloud.timestamp
            }
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
            pointCloudShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
            render.draw(pointCloudMesh, pointCloudShader)
        }

        // Visualize planes.
        planeRenderer.drawPlanes(
            render,
            session.getAllTrackables(Plane::class.java),
            camera.displayOrientedPose,
            projectionMatrix
        )

        // -- Draw occluded virtual objects

        // Update lighting parameters in the shader
        //updateLightEstimation(frame.lightEstimate, viewMatrix)

        // Visualize anchors created by touch.
        render.clear(virtualSceneFramebuffer, 0f, 0f, 0f, 0f)

        //render all models
        renderAllModels(render)

        // Compose the virtual scene with the background.
        backgroundRenderer.drawVirtualScene(render, virtualSceneFramebuffer, Z_NEAR, Z_FAR)
    }

    /** Checks if we detected at least one plane. */
    private fun Session.hasTrackingPlane() =
        getAllTrackables(Plane::class.java).any { it.trackingState == TrackingState.TRACKING }

    // Handle only one tap per frame, as taps are usually low frequency compared to frame rate.
    /**
     * @brief   callback function to handle tapping on the screen
     *
     * @param   frame   The current frame
     *
     * @param   camera  The current camera rendering
     *
     */
    private fun handleTap(frame: Frame, camera: Camera) {

        if (!mainViewModel.isAllowUserTappingToPlace.value) return
        if (camera.trackingState != TrackingState.TRACKING) return
        val tap = fragment.sofaArView.tapHelper.poll() ?: return
        val instantPlacementSettings = fragment.instantPlacementSettings

        val hitResultList =
            if (instantPlacementSettings.isInstantPlacementEnabled) {
                frame.hitTestInstantPlacement(tap.x, tap.y, APPROXIMATE_DISTANCE_METERS)
            } else {
                frame.hitTest(tap)
            }

        // Hits are sorted by depth. Consider only closest hit on a plane, Oriented Point, Depth Point,
        // or Instant Placement Point.
        val firstHitResult =
            hitResultList.firstOrNull { hit ->
                when (val trackable = hit.trackable!!) {
                    is Plane ->
                        trackable.isPoseInPolygon(hit.hitPose) &&
                                PlaneRenderer.calculateDistanceToPlane(hit.hitPose, camera.pose) > 0

                    is Point -> trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL
                    is InstantPlacementPoint -> true
                    // DepthPoints are only returned if Config.DepthMode is set to AUTOMATIC.
                    is DepthPoint -> true
                    else -> false
                }
            }

        if (firstHitResult != null) {
            // Cap the number of objects created. This avoids overloading both the
            // rendering system and ARCore.
            if (wrappedAnchors.size >= 20) {
                wrappedAnchors[0].anchor.detach()
                wrappedAnchors.removeAt(0)
            }


            // Adding an Anchor tells ARCore that it should track this position in
            // space. This anchor is created on the Plane to place the 3D model
            // in the correct position relative both to the world and to the plane.
            wrappedAnchors.add(
                WrappedAnchor(
                    firstHitResult.createAnchor(),
                    firstHitResult.trackable,
                    mainViewModel.currentlySelectedInformation.value.modelIndex,
                    mainViewModel.latestAnchorInformation.value.scale,
                    mainViewModel.latestAnchorInformation.value.position,
                    mainViewModel.latestAnchorInformation.value.rotation
                )
            )

            // reset our values
            // should be fine as long as this isn't multithreaded
            mainViewModel.updateLatestAnchorInformation(
                AnchorModificationInformation(
                    position = Vec3(0f, 0f, 0f),
                    scale = Vec3(1f, 1f, 1f),
                    rotation = Vec3(0f, 0f, 0f)
                )
            )

            // For devices that support the Depth API, shows a dialog to suggest enabling
            // depth-based occlusion. This dialog needs to be spawned on the UI thread.
            activity.runOnUiThread { fragment.sofaArView.showOcclusionDialogIfNeeded() }
        }
    }

    private fun showError(errorMessage: String) =
        fragment.sofaArView.snackbarHelper.showError(activity, errorMessage)
}

/* ---------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------- */
