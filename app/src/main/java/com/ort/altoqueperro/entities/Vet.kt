package com.ort.altoqueperro.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Vet(
    var name: String,
    var coordinates: Coordinates?,
    var address: String?,
    var phone: String?,
    var localidad: String?,
    var description: String?,
    var businessHours: String?,
    var imageUrl: String?,
) : Parcelable {

    constructor() : this("", null, "", "", "", "", "", "")

}