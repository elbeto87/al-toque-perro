package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.repos.RequestRepository
import com.ort.altoqueperro.repos.RescueCenterRepository
import com.ort.altoqueperro.utils.Notifications

class NewMapModeViewModel : ViewModel() {
    var requestRepository: MutableLiveData<MutableList<LostPetRequest>> =
        MutableLiveData(mutableListOf())
    var vetsRepository: MutableLiveData<MutableList<Vet>> = MutableLiveData(mutableListOf())
    var sheltersRepository: MutableLiveData<MutableList<Shelter>> = MutableLiveData(mutableListOf())

    val user = Firebase.auth.currentUser

    fun getLostPets() {
        RequestRepository.getAllLostPetRequests(requestRepository)
    }

    fun getShelters() {
        RescueCenterRepository.getShelters(sheltersRepository)
    }

    fun getVets() {
        RescueCenterRepository.getVets(vetsRepository)

    }

    fun filterRequests(): MutableList<LostPetRequest> {
        val filteredRequestRepository: MutableList<LostPetRequest> = requestRepository.value!!
        if (Notifications.getNotificationPetLost()) {
            filteredRequestRepository.removeAll { shouldRemove(it) }
        }
        return filteredRequestRepository
    }

    private fun shouldRemove(it: LostPetRequest): Boolean {
        val userId: String = user?.uid.toString()
        return it.requestCreator == userId
    }
}

