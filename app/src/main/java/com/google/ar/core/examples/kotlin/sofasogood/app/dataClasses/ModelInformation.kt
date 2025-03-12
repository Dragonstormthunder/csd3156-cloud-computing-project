/**
@file
ModelInformation.kt
@author
Goh Jun Lin Wayne 2200628
@par Email
junlinwayne.goh@digipen.edu
@date
30/01/25
@par Course
CSD3156 Mobile and Cloud Computing
@brief
This file contains data used by the application for rendering
 */

package com.google.ar.core.examples.kotlin.sofasogood.app.dataClasses

import com.google.ar.core.examples.java.common.samplerender.Mesh
import com.google.ar.core.examples.java.common.samplerender.Texture
import com.google.ar.core.examples.kotlin.helloar.R

/**
 * basic data class to contain model information for rendering
 *
 * @property    index
 *              The index of the item in the list. also used as the ID
 *              for the model.
 *
 * @property    modelPath
 *
 * @property    texturePath
 *
 * @property    displayPath
 *
 * @property    name
 *
 * @property    mesh
 *
 * @property    texturePath
 */
data class ModelInformation(
    val index: Int,
    val modelPath: String,
    val texturePath: String,
    var displayPath: Int,
    var name: String,
    var mesh: Mesh? = null,
    var texture: Texture? = null,
    var initialScale: Vec3 = Vec3(1f, 1f, 1f)
)

//list of all currently available model information for rendering
var listModelInformation = listOf(

//    //0th model
//    ModelInformation(
//        "models/pawn.obj",
//        "models/pawn_albedo.png",
//        0,
//        "Pawn"
//    ),

    //1st model
    ModelInformation(
        0, // 0th in the list
        "models/Cube.obj",
        "models/rock4.png",
        R.drawable.rock_cube,
        "Cube",
        initialScale = Vec3(0.1f, 0.1f, 0.1f),
    ),

    //2nd model chair
    ModelInformation(
        1,  // 1st in the list
        "models/Chair.obj",
        "models/Chair-1.jpg",
        R.drawable.chair_front,
        "Chair",
        initialScale = Vec3(0.1f, 0.1f, 0.1f)
    ),

    //3rd model sofa
    ModelInformation(
        2,  // 2nd in the list
        "models/SofaSoGood.obj",
        "models/SofaSoGood.jpg",
        R.drawable.koltuk1,
        "Sofa",
        initialScale = Vec3(0.7f, 0.7f, 0.7f)
    ),

    //4th model bed
    ModelInformation(
        3,  // 3rd in the list
        "models/Bed.obj",
        "models/SofaSoGood.jpg",
        R.drawable.bed,
        "Bed",
        initialScale = Vec3(0.7f, 0.7f, 0.7f)
    ),

    //5th model table
    ModelInformation(
        4,  // 4th in the list
        "models/Table.obj",
        "models/WoodSeemles.jpg",
        R.drawable.table,
        "Table",
        initialScale = Vec3(0.3f, 0.3f, 0.3f)
    ),


    )

/**
 * Holds the currently selected information.
 *
 * @property    modelIndex
 *              The model index that indexes into listModelInformation.
 */
data class SelectionInformation(
    var modelIndex: Int,
//    var scale: Vec3,
//    var position: Vec3,
//    var rotation: Vec3
)

/**
 * The offsets/multipliers to the model's data.
 *
 * @property    scale
 *              A multiplier to the model's scale.
 *
 * @property    position
 *              An offset to the model's position.
 *
 * @property    rotation
 *              An offset to the model's rotation in degrees.
 */
data class AnchorModificationInformation(
    val scale: Vec3,
    val position: Vec3,
    val rotation: Vec3
)

/**
 * basic data class to represent Vec3
 *
 * @param   x
 *          The x-coordinate.
 *
 * @param   y
 *          The y-coordinate.
 *
 * @param   z
 *          The z-coordinate.
 */
data class Vec3(
    var x: Float,
    var y: Float,
    var z: Float
) {

    /**
     * Overloaded for multiplication.
     *
     * @param   scalar
     *          Every component will be scaled by this scalar.
     */
    operator fun times(scalar: Float): Vec3 {
        return Vec3(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Overloaded for component-wise multiplication.
     *
     * @param   other
     *          The other vector.
     */
    operator fun times(other: Vec3): Vec3 {
        return Vec3(x * other.x, y * other.y, z * other.z)
    }

}