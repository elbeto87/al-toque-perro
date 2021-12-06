package com.ort.altoqueperro.entities

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize

@Parcelize
class Coordinates() : Parcelable {
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    constructor(latitude:Double,longitude:Double) : this() {
        this.latitude = latitude
        this.longitude = longitude
    }
    @Exclude
    fun getLatLng(): LatLng {
        var latlng = LatLng(latitude,longitude)
        return latlng
    }
    fun getGeoPoint(): GeoPoint {
        var geoPoint = GeoPoint(latitude,longitude)
        return geoPoint
    }
}