/**
 * @file    MainViewModel.kt
 * @author  Emma Natalie Soh
 * @par     Email: 2202191@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   The view model for this application.
 */

/* packages
------------------------------------------------------------------------------------------------- */

package com.google.ar.core.examples.kotlin.sofasogood.app

/* functions
------------------------------------------------------------------------------------------------- */

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Config
import com.google.ar.core.Config.InstantPlacementMode
import com.google.ar.core.examples.java.common.helpers.InstantPlacementSettings
import com.google.ar.core.examples.kotlin.common.helpers.ARCoreSessionLifecycleHelper
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.AnchorModificationInformation
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.SelectionInformation
import com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses.Vec3
import com.google.ar.core.examples.kotlin.sofasogood.repo.SofaSoGoodEntity
import com.google.ar.core.examples.kotlin.sofasogood.repo.SofaSoGoodRepository
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/* ---------------------------------------------------------------------------------------------- */

/**
 * @brief   The main view model.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SofaSoGoodRepository
) : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // display object browser boolean

    /**
     * The state of whether the display object browser overlay flow is active.
     */
    private val displayObjectBrowserOverlayFlow = MutableStateFlow(true)

    /**
     * The state flow to monitor the state of displayObjectBrowserOverlayFlow.
     */
    val displayObjectBrowserOverlay: StateFlow<Boolean> = displayObjectBrowserOverlayFlow

    /**
     * Updates the state of the display object browser overlay.
     *
     * @param   isDisplayed
     *          The display value.
     */
    fun updateDisplayObjectBrowserOverlay(isDisplayed: Boolean) {
        displayObjectBrowserOverlayFlow.value =
            isDisplayed // Emit a new value to trigger recomposition
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // allow user tapping

    /**
     * Whether the placing is engaged or scaling is engaged.
     */
    private val isAllowUserTappingToPlaceFlow = MutableStateFlow(true)

    /**
     * The state flow to monitor the state of isAllowUserTappingToPlace.
     */
    val isAllowUserTappingToPlace: StateFlow<Boolean> = isAllowUserTappingToPlaceFlow

    /**
     * Updates the state of whether the user is allowed to place furniture.
     *
     * @param   isPlacingAllowed
     *          Whether placing is allowed.
     */
    fun updateAllowUserTappingToPlace(isPlacingAllowed: Boolean) {
        isAllowUserTappingToPlaceFlow.value =
            isPlacingAllowed // Emit a new value to trigger recomposition
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // anchor information

    /**
     * Modify this data to modify the last placed object in the AR application.
     */
    private val latestAnchorInformationFlow = MutableStateFlow(
        AnchorModificationInformation(
            Vec3(1f, 1f, 1f),
            Vec3(0f, 0f, 0f),
            Vec3(0f, 0f, 0f)
        )
    )

    /**
     * The state flow to monitor the state of latestAnchorInformationFlow.
     */
    val latestAnchorInformation: StateFlow<AnchorModificationInformation> =
        latestAnchorInformationFlow

    /**
     * Updates the data to modify the last placed object in the AR application.
     *
     * @param   latestAnchorInformation
     *          The latest anchor information.
     */
    fun updateLatestAnchorInformation(latestAnchorInformation: AnchorModificationInformation) {
        latestAnchorInformationFlow.value =
            latestAnchorInformation // Emit a new value to trigger recomposition
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // anchor information

    /**
     * This variable tells you which model is currently selected from the object
     * browser.
     */
    private val currentlySelectedInformationFlow = MutableStateFlow(
        SelectionInformation(
            modelIndex = 0,
        )
    )

    /**
     * The state flow to monitor the state of currentlySelectedInformationFlow.
     */
    val currentlySelectedInformation: StateFlow<SelectionInformation> =
        currentlySelectedInformationFlow

    /**
     * Updates the data to modify the currently selected information for the model.
     *
     * @param   currentlySelectedInformation
     *          The currently selected information.
     */
    fun updateCurrentlySelectedInformation(currentlySelectedInformation: SelectionInformation) {
        currentlySelectedInformationFlow.value =
            currentlySelectedInformation // Emit a new value to trigger recomposition
    }

    /* ------------------------------------------------------------------------------------------ */

//    override fun onCleared() {
//        // supposedly should close? but arCoreSessionHelper is managing this
//        // and i'm not sure if I'm supposed to touch it.
//        // arCoreSessionHelper.session?.close()
//
//        super.onCleared()
//    }

    /* ------------------------------------------------------------------------------------------ */

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // user preferences
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* ------------------------------------------------------------------------------------------ */

    // Keep the user preferences as a stream of changes
//    val userPreferencesFlow = userPreferencesManager.userPreferencesFlow.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(), // when to start collecting
//        initialValue = UserPreferences(
//            lastObjectID = -1
//        )
//    )
//    val userPreferencesFlow = userPreferencesManager.userPreferencesFlow.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(), // when to start collecting
//        initialValue = UserPreferences(
//            lastObjectID = -1
//        )
//    )

    /* ------------------------------------------------------------------------------------------ */

    val allSofas = repository.allSofas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Timeout for WhileSubscribed
        initialValue = emptyList()
    )

    private val _qrCode = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val card: StateFlow<SofaSoGoodEntity?> = _qrCode
        .flatMapLatest { qrCode ->
            if (qrCode != null) {
                repository.getSofaByQrCode(qrCode)
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setQrCode(qrCode: String) {
        Log.d("ViewModel", "QR Code Scanned: $qrCode")
        _qrCode.value = qrCode
        viewModelScope.launch {
            repository.unlockSofa(qrCode) // Unlock the sofa if it exists
        }
    }

    fun insert(sofa: SofaSoGoodEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(sofa)
    }

//    fun update(sofa: SofaSoGoodEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.update(sofa)
//    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // Update the isUnlocked field for a specific sofa
//    suspend fun unlockSofa(qrCode: String) {
//        val sofa = repoDao.getSofaByQrCode(qrCode).firstOrNull()
//        if (sofa != null && !sofa.isUnlocked) {
//            val updatedSofa = sofa.copy(isUnlocked = true)
//            sofaDao.updateSofa(updatedSofa)
//        }
//    }
    /* ------------------------------------------------------------------------------------------ */

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ar session
    ////////////////////////////////////////////////////////////////////////////////////////////////

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
        private set

    /**
     * Initializes the ARCoreSessionHelper.
     *
     * @param   activity
     *          The activity context.
     *
     * @param   instantPlacementSettings
     *          The initial placement settings.
     */
    fun initializeARCoreSessionHelper(
        activity: Activity,
        instantPlacementSettings: InstantPlacementSettings
    ) {

        // Create the helper using the activity context.
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(activity).apply {
            // Set an exception callback to show errors.
            exceptionCallback = { exception ->
                val message = when (exception) {
                    is UnavailableUserDeclinedInstallationException ->
                        "Please install Google Play Services for AR"

                    is UnavailableApkTooOldException ->
                        "Please update ARCore"

                    is UnavailableSdkTooOldException ->
                        "Please update this app"

                    is UnavailableDeviceNotCompatibleException ->
                        "This device does not support AR"

                    is CameraNotAvailableException ->
                        "Camera not available. Try restarting the app."

                    else ->
                        "Failed to create AR session: $exception"
                }
                Log.e("HelloArViewModel", "ARCore exception: $message", exception)
                // Optionally, send this message to the fragment via a LiveData or similar.
            }

            // Configure the session before it resumes.
            beforeSessionResume = { session ->
                session.configure(
                    session.config.apply {
                        lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                        depthMode =
                            if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC))
                                Config.DepthMode.AUTOMATIC
                            else Config.DepthMode.DISABLED
                        instantPlacementMode =
                            if (instantPlacementSettings.isInstantPlacementEnabled)
                                InstantPlacementMode.LOCAL_Y_UP
                            else InstantPlacementMode.DISABLED
                    }
                )
            }

        }

    }

}

/* ---------------------------------------------------------------------------------------------- */

//class MainViewModelFactory(
//    private val userPreferencesManager: UserPreferencesManager
//) : ViewModelProvider.Factory {
//
//    override fun<T: ViewModel> create(modelClass: Class<T>) : T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(userPreferencesManager) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//class MainViewModelFactory(
//    private val userPreferencesManager: UserPreferencesManager
//) : ViewModelProvider.Factory {
//
//    override fun<T: ViewModel> create(modelClass: Class<T>) : T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(userPreferencesManager) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

/* ------------------------------------------------------------------------------------------ */
