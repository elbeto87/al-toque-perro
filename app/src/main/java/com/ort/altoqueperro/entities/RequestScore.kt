package com.ort.altoqueperro.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RequestScore(val request: FoundPetRequest?, val score: Int) : Parcelable{

}