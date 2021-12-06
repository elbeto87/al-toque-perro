package com.ort.altoqueperro.viewmodels

import android.content.Context
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.Pet
import com.ort.altoqueperro.repos.RequestRepository
import com.ort.altoqueperro.utils.ImageHelper
import com.ort.altoqueperro.utils.ServiceLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PetFoundViewModel : ViewModel() {

    private val mutableComments = MutableLiveData<String>()
    private var request: FoundPetRequest? = null
    val comments: LiveData<String> get() = mutableComments

    fun setComments(value: String) {
        if (value.isEmpty()) {
            mutableComments.value = "Sin comentarios"
        } else {
            mutableComments.value = value
        }
    }

    fun setRequest(request: FoundPetRequest) {
        this.request = request
        val pet: Pet = request.pet
        pet.comments?.let { setComments(it) }
        setLostDate(pet.lostDate)
        setPetEyeColor(pet.eyes)
        setPetFurColor(pet.furColor)
        setPetFurLength(pet.furLength)
        setPetNose(pet.nose)
        setPetSex(pet.sex)
        setPetSize(pet.size)
        setPetType(pet.type)
        if (Uri.EMPTY.equals(mutablePhoto.value)) setPhoto(request.imageURL!!.toUri())
    }

    private val mutablePetEyeColor = MutableLiveData<String>()
    val petEyeColor: LiveData<String> get() = mutablePetEyeColor

    fun setPetEyeColor(value: String) {
        mutablePetEyeColor.value = value
    }

    private val mutablePetFurColor = MutableLiveData<String>()
    val petFurColor: LiveData<String> get() = mutablePetFurColor

    fun setPetFurColor(value: String) {
        mutablePetFurColor.value = value
    }

    private val mutablePetFurLength = MutableLiveData<String>()
    val petFurLength: LiveData<String> get() = mutablePetFurLength

    fun setPetFurLength(value: String) {
        mutablePetFurLength.value = value
    }

    private val mutablePetNose = MutableLiveData<String>()
    val petNose: LiveData<String> get() = mutablePetNose

    fun setPetNose(value: String) {
        mutablePetNose.value = value
    }

    private val mutablePetSex = MutableLiveData<String>()
    val petSex: LiveData<String> get() = mutablePetSex

    fun setPetSex(value: String) {
        mutablePetSex.value = value
    }

    private val mutablePetSize = MutableLiveData<String>()
    val petSize: LiveData<String> get() = mutablePetSize

    fun setPetSize(value: String) {
        mutablePetSize.value = value
    }

    private val mutablePetType = MutableLiveData<String>()
    val petType: LiveData<String> get() = mutablePetType

    fun setPetType(value: String) {
        mutablePetType.value = value
    }

    private val mutableLostDate = MutableLiveData<String>()
    val lostDate: LiveData<String> get() = mutableLostDate

    fun setLostDate(value: String) {
        mutableLostDate.value = value
    }

    private val mutablePhoto = MutableLiveData<Uri>()
    val photo: LiveData<Uri> get() = mutablePhoto
    fun setPhoto(value: Uri) {
        mutablePhoto.value = value
    }

    fun registerPet() {
        val user = Firebase.auth.currentUser
        val pet = Pet(
            null,
            petType.value.toString(),
            petSize.value.toString(),
            petSex.value.toString(),
            petNose.value.toString(),
            petFurLength.value.toString(),
            petFurColor.value.toString(),
            petEyeColor.value.toString(),
            comments.value.toString(),
            lostDate.value.toString()
        )

        val petRequest: FoundPetRequest
        if (request != null) {
            request!!.pet = pet
            petRequest = request!!
        } else {
            petRequest = FoundPetRequest(
                pet,
                null,
                user!!.uid,
            )
        }
        //upload image
        if (petRequest.coordinates == null) petRequest.coordinates = ServiceLocation.getLocation()
        viewModelScope.launch(Dispatchers.IO) {
            if (!Uri.EMPTY.equals(photo.value) && petRequest.imageURL?.toUri() != photo.value) {
                petRequest.imageURL = ImageHelper().storeImage(photo.value!!)
            }
            saveRequest(petRequest)
        }
    }


    fun validateStep1(): Boolean {
        return !mutablePetSex.value.isNullOrEmpty() && !mutablePetSize.value.isNullOrEmpty() && !mutablePetType.value.isNullOrEmpty() && !Uri.EMPTY.equals(
            mutablePhoto.value
        )
    }

    fun validateStep2(): Boolean {
        return !mutablePetEyeColor.value.isNullOrEmpty() && !mutablePetFurColor.value.isNullOrEmpty() && !mutablePetFurLength.value.isNullOrEmpty() && !mutablePetNose.value.isNullOrEmpty()
    }

    fun validateStep3(): Boolean {
        return !mutableLostDate.value.isNullOrEmpty()
    }

    private fun saveRequest(petRequest: FoundPetRequest) {
        RequestRepository.saveFoundPetRequest(petRequest)
    }

    fun setSpinner(liveData: LiveData<String>, array: Int, context: Context, spinner: Spinner) {

        val selectedValue: String? = liveData.value

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context,
            array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if (selectedValue != null) {
            val spinnerPosition: Int = adapter.getPosition(selectedValue)
            spinner.setSelection(spinnerPosition)
        }
    }

    fun setRadioButton(radioGroup: RadioGroup, liveData: LiveData<String>) {
        val selectedValue: String? = liveData.value
        radioGroup.children.forEach {
            val radioButton = it as RadioButton
            if (selectedValue != null && radioButton.text == selectedValue) {
                radioButton.isChecked = true
            }
        }
    }

    fun clearALl() {
        this.request = null
        setComments("")
        setLostDate("")
        setPetEyeColor("")
        setPetFurColor("")
        setPetFurLength("")
        setPetNose("")
        setPetSex("")
        setPetSize("")
        setPetType("")
        setPhoto(Uri.EMPTY)
    }


}