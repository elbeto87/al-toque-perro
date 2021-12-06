package com.ort.altoqueperro.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    var id: String,
    var name: String,
    var email: String,
    var phone: String,
    var birth: String
) : Parcelable {

    constructor() : this("", "", "", "", "")

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Usuario(nombre=$name, email=$email, phone=$phone, birthDate=$birth)"
    }


}
