package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.repos.RequestRepository

class SimilarPetViewModel : ViewModel() {

    fun updateRequests(foundRequest: FoundPetRequest?, lostRequest: LostPetRequest) {
        RequestRepository.saveLostPetRequest(lostRequest)
        if (foundRequest != null) RequestRepository.saveFoundPetRequest(foundRequest)
    }
}