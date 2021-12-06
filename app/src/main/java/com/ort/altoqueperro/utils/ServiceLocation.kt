package com.ort.altoqueperro.utils

import android.location.Location
import com.google.firebase.firestore.GeoPoint
import com.ort.altoqueperro.entities.Coordinates

object ServiceLocation {
    var location:Location = Location("null")
    init {
        println("Singleton class invoked.")
    }
    fun getLocation(): Coordinates? {
        if (!location.provider.equals("null")) {
            return Coordinates(
                location.latitude,
                location.longitude
            )
        }
        return null
    }
    fun getDistance(to:Coordinates?):Float{
        if (to == null) {
            return 0f
        }
        //retorna distancia en metros
        val from = Location("")
        var latlng = getLocation()
        if (latlng == null) return 0f
        from.setLatitude(latlng.latitude)
        from.setLongitude(latlng.longitude)

        val loc_to = Location("")
        loc_to.setLatitude(to.latitude)
        loc_to.setLongitude(to.longitude)
        return from.distanceTo(loc_to)
    }
}