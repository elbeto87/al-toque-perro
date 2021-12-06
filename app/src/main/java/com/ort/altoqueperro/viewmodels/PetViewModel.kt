package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.repos.UserRepository

class PetViewModel : ViewModel() {
    var userLiveData: MutableLiveData<User> = MutableLiveData()

    fun getUser(uuid: String?) {
        if (uuid.isNullOrBlank()) return
        UserRepository.getUser(userLiveData, uuid)
    }
}