package com.ort.altoqueperro.entities

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
class Shelter(
    var name: String,
    var phoneNumber: String,
    var coordinates: Coordinates?,
    var imageUrl: String?,
    var hoster: String?,
    var capMax: String?,
    var address: String,
    var localidad: String?,
    var notes: String?,
) :
    Parcelable {

    constructor() : this("", "", null, "", "","","","","")
}

