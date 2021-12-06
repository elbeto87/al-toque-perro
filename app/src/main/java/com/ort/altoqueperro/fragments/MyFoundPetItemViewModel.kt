package com.ort.altoqueperro.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.repos.RequestRepository

class MyFoundPetItemViewModel : ViewModel() {
    var requestConsumerLiveData: MutableLiveData<LostPetRequest> = MutableLiveData()


    fun getRequestConsumer(uuid: String?) {
        if (uuid.isNullOrBlank()) return
        RequestRepository.getLostPetRequest(requestConsumerLiveData, uuid)
    }

}