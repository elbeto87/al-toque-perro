package com.ort.altoqueperro.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ComparingScore(
    val sex: Int,
    val size: Int,
    val nose: Int,
    var coatSize: Int,
    var coatColor: Int,
    val eyeColor: Int,
    val minScoreValue: Int
) : Parcelable {

    constructor() : this(0,0, 0, 0, 0, 0, 0)
}