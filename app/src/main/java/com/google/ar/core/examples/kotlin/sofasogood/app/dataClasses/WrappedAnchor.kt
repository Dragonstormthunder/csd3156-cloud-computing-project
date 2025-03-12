/**
 * @file    WrappedAnchor.kt
 * @author  Goh Jun Lin Wayne 2200628
 * @par     Email: junlinwayne.goh@digipen.edu
 * @date    30/01/25
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @brief   This file contains data used by the application for rendering
 */

package com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses

import com.google.ar.core.Anchor
import com.google.ar.core.Trackable

/**
 * Associates an Anchor with the trackable it was attached to. This is used to be able to check
 * whether or not an Anchor originally was attached to an {@link InstantPlacementPoint}.
 *
 * This is basically a class that represents the object in the application
 *
 * @property    anchor
 *
 * @property    trackable
 *
 * @property    model
 *
 * @property    scale
 *
 * @property    position
 *
 * @property    rotation
 */
data class WrappedAnchor(
    val anchor: Anchor,
    val trackable: Trackable,
    val model: Int,
    var scale: Vec3,
    var position: Vec3,
    var rotation: Vec3,
)