package com.ort.altoqueperro.entities

import com.ort.altoqueperro.entities.State.OPEN
import com.ort.altoqueperro.entities.State.PENDING
import java.util.*

class LostPetRequest(
    id: String,
    pet: Pet,
    state: Int,
    creationDate: Date,
    resolvedDate: Date?,
    coordinates: Coordinates?,
    requestCreator: String,
    requestConsumer: String?,
    imageURL: String?,
) : PetRequest(
    id,
    pet,
    state,
    creationDate,
    resolvedDate,
    coordinates,
    requestCreator,
    requestConsumer,
    imageURL
) {

    constructor() : this(
        "",
        Pet(), 1,
        Calendar.getInstance().time, null, null, "", null, null
    )

    constructor(
        pet: Pet,
        coordinates: Coordinates?,
        requestCreator: String
    ) : this(
        "",
        pet, OPEN.ordinal,
        Calendar.getInstance().time, null, coordinates, requestCreator, null, null
    )

    private fun setPending(other: PetRequest) {
        super.setPending()
        this.requestConsumer = other.id
        other.requestConsumer = this.id
    }


    fun nextStateConfirm(other: PetRequest) {
        when (findRequestState()) {
            OPEN -> setPending(other)
            PENDING -> closeRequest(other)
            else -> print("la request está cerrada")
        }
    }

    fun nextStateConfirm() {
        if (isOpen()) closeRequest()
        else print("la request está cerrada")
    }

    fun nextStateCancel(other: PetRequest) {
        when (findRequestState()) {
            PENDING -> openRequest(other)
            else -> print("la request está cerrada o abierta")
        }
    }

    override fun closeRequest(other: PetRequest) {
        other.closeRequest()
        super.closeRequest()
    }

    override fun openRequest(other: PetRequest) {
        other.openRequest()
        super.openRequest()
    }

}