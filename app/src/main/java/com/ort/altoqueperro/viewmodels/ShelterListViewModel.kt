package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.repos.RescueCenterRepository

class ShelterListViewModel : ViewModel() {
    var sheltersLiveData: MutableLiveData<MutableList<Shelter>> = MutableLiveData(mutableListOf())


    fun getShelters() {
        RescueCenterRepository.getShelters(sheltersLiveData)
    }
}