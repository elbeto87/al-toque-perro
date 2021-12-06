package com.ort.altoqueperro.repos

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.utils.ServiceLocation

object RescueCenterRepository {
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun getVets(liveData: MutableLiveData<MutableList<Vet>>) {
        val vets: MutableList<Vet> = mutableListOf()
        db.collection("vets").get()
            .addOnSuccessListener {
                for (vet in it) {
                    vets.add(vet.toObject())
                }
                liveData.postValue(vets.sortedBy { vet -> ServiceLocation.getDistance(vet.coordinates) } as MutableList<Vet>)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    fun getShelters(liveData: MutableLiveData<MutableList<Shelter>>) {
        val shelters: MutableList<Shelter> = mutableListOf()
        db.collection("shelters").get()
            .addOnSuccessListener {
                for (shelter in it) {
                    shelters.add(shelter.toObject())
                }
                liveData.postValue(shelters.sortedBy { shelter -> ServiceLocation.getDistance(shelter.coordinates) } as MutableList<Shelter>)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}