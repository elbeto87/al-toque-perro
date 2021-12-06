package com.ort.altoqueperro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.repos.UserRepository

class SignUpViewModel : ViewModel() {
    var success: MutableLiveData<String> = MutableLiveData()

    fun registerUser(
        name: String?,
        password: String?,
        mail: String?,
        phone: String?,
        birth: String?,
        passwordVerify: String?
    ) {
        if (!name.isNullOrEmpty() && !password.isNullOrEmpty() && !mail.isNullOrEmpty() && !phone.isNullOrEmpty() && !birth.isNullOrEmpty() && !passwordVerify.isNullOrEmpty()) {
            if (password == passwordVerify) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
                    .addOnSuccessListener {
                        val id = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        UserRepository.saveUser(User(id, name, mail, phone, birth), success)
                    }
                    .addOnFailureListener { exception -> success.postValue(exception.message) }
            } else success.postValue("Las contraseñas no coinciden")

        } else success.postValue("Por favor completar todos los campos")
    }

}