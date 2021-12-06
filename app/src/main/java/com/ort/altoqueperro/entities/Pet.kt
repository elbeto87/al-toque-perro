package com.ort.altoqueperro.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Pet(
    var name: String?,
    var type: String,
    var size: String,
    var sex: String,
    var nose: String,
    var furLength: String,
    var furColor: String,
    var eyes: String,
    var comments: String?,
    var lostDate: String
) : Parcelable {

    constructor() : this("", "", "", "", "", "", "", "", "", "")

    fun compareTo(other: Pet, comparingScore: ComparingScore): Int {
        var currentScore = 0
        if (type == other.type) {
            if (size == other.size) currentScore += comparingScore.size
            if (sex == other.sex) currentScore += comparingScore.sex
            if (nose == other.nose) currentScore += comparingScore.nose
            if (furLength == other.furLength) currentScore += comparingScore.coatSize
            if (furColor == other.furColor) currentScore += comparingScore.coatColor
            if (eyes == other.eyes) currentScore += comparingScore.eyeColor
        }
        return currentScore
    }

    override fun toString(): String {
        return "$type $size, $sex, Color $furColor, $comments"
    }
}