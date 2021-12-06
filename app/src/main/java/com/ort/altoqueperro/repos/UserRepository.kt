package com.ort.altoqueperro.repos

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.entities.User

object UserRepository {
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun getUser(liveData: MutableLiveData<User>, userUuid: String) {
        db.collection("users").document(userUuid).get()
            .addOnSuccessListener {
                liveData.postValue(it.toObject())
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    fun saveUser(user: User, mutableLiveData: MutableLiveData<String>) {
        db.collection("users").document(user.id).set(user)
            .addOnSuccessListener {
                mutableLiveData.postValue("success")
            }
            .addOnFailureListener {
                    exception ->
                println("Error saving document: ${exception.message}")
                mutableLiveData.postValue("Error creating User: ${exception.message}")
            }
    }

}