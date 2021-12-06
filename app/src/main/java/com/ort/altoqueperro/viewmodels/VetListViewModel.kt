package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.repos.RescueCenterRepository

class VetListViewModel : ViewModel() {
    var vetsLiveData: MutableLiveData<MutableList<Vet>> = MutableLiveData(mutableListOf())


    fun getVets() {
        RescueCenterRepository.getVets(vetsLiveData)
    }

}