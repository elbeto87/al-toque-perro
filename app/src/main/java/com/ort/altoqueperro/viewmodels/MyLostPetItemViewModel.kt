package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.repos.RequestRepository

class MyLostPetItemViewModel : ViewModel() {
    var requestConsumerLiveData: MutableLiveData<FoundPetRequest> = MutableLiveData()


    fun getRequestConsumer(uuid: String?) {
        if (uuid.isNullOrBlank()) return
        RequestRepository.getFoundPetRequest(requestConsumerLiveData, uuid)
    }

    fun updateRequests(foundRequest: FoundPetRequest?, lostRequest: LostPetRequest) {

        RequestRepository.saveLostPetRequest(lostRequest)
        if (foundRequest != null) RequestRepository.saveFoundPetRequest(foundRequest)
    }
}