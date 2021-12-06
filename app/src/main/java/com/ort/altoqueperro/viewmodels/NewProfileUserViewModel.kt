package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.repos.UserRepository

class NewProfileUserViewModel : ViewModel() {
    var userLiveData: MutableLiveData<User> = MutableLiveData()
    var success: MutableLiveData<String> = MutableLiveData()


    fun getUser(uuid: String?) {
        if (uuid.isNullOrBlank()) return
        UserRepository.getUser(userLiveData, uuid)
    }

    fun editUser(name: String, phone: String, birth: String) {
        val user: User? = userLiveData.value
        if (user != null) {
            user.name = name
            user.phone = phone
            user.birth = birth
            UserRepository.saveUser(user, success)
        }
    }
}