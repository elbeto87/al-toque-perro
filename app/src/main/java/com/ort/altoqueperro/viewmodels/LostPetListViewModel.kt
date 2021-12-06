package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.repos.RequestRepository
import com.ort.altoqueperro.utils.Notifications
import com.ort.altoqueperro.utils.ServiceLocation

class LostPetListViewModel : ViewModel() {
    var requestRepository: MutableLiveData<MutableList<LostPetRequest>> =
        MutableLiveData(mutableListOf())
    var foundRequestsRepository: MutableLiveData<MutableList<FoundPetRequest>> =
        MutableLiveData(mutableListOf())
    var myLostRequestsRepository: MutableList<LostPetRequest> = mutableListOf()
    var othersLostRequestsRepository: MutableList<LostPetRequest> = mutableListOf()
    val user = FirebaseAuth.getInstance().currentUser

    fun getLostPets() {
        RequestRepository.getAllLostPetRequests(requestRepository)
    }

    fun getOwnFoundPets() {
        RequestRepository.getOwnFoundPetRequests(foundRequestsRepository, user?.uid.toString())
    }

    fun filterRequests() {
        myLostRequestsRepository.clear()
        othersLostRequestsRepository.clear()
        if (!Notifications.getNotificationPetLost()) {
            myLostRequestsRepository =
                requestRepository.value!!.filter { shouldRetain(it) } as MutableList<LostPetRequest>
        }
        othersLostRequestsRepository =
            requestRepository.value!!.filter { !shouldRetain(it) } as MutableList<LostPetRequest>
        othersLostRequestsRepository.sortBy { ServiceLocation.getDistance(it.coordinates) }
    }

    private fun shouldRetain(it: LostPetRequest): Boolean {
        val userId: String = user?.uid.toString()
        return it.requestCreator == userId
    }
}