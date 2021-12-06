package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.repos.UserRepository

class LostPetItemViewModel : ViewModel() {
    var userLiveData: MutableLiveData<User> = MutableLiveData()

    fun getUser(userId: String) {
        UserRepository.getUser(userLiveData, userId)
    }
}