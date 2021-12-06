package com.ort.altoqueperro.entities

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class PetRequest(
    var id: String,
    var pet: Pet,
    var state: Int,
    var creationDate: Date,
    var resolvedDate: Date?,
    var coordinates: Coordinates?,
    var requestCreator: String,
    var requestConsumer: String?,
    var imageURL: String?
) : Parcelable {

    constructor() : this(
        "",
        Pet(),
        State.OPEN.ordinal,
        Calendar.getInstance().time,
        null,
        null,
        "",
        null,
        null
    )

    fun comparePetTo(other: PetRequest, comparingScore: ComparingScore): Int {
        return pet.compareTo(other.pet, comparingScore)
    }

    open fun closeRequest() {
        this.changeRequestState(State.CLOSED)
        this.resolvedDate = Calendar.getInstance().time
    }

    open fun openRequest() {
        this.requestConsumer = null
        this.resolvedDate = null
        this.changeRequestState(State.OPEN)
    }

    open fun setPending() {
        this.changeRequestState(State.PENDING)
    }

    open fun findRequestState(): State {
        return State.values()[this.state]
    }

    open fun changeRequestState(state: State) {
        this.state = state.ordinal
    }


    open fun closeRequest(other: PetRequest) {}
    open fun openRequest(other: PetRequest) {}

    @Exclude
    open fun isPending(): Boolean {
        return this.findRequestState() == State.PENDING
    }

    @Exclude
    open fun isClosed(): Boolean {
        return this.findRequestState() == State.CLOSED

    }

    @Exclude
    open fun isOpen(): Boolean {
        return this.findRequestState() == State.OPEN

    }
}