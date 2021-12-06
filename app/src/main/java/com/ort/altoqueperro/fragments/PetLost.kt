package com.ort.altoqueperro.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.utils.ImageHelper
import com.ort.altoqueperro.viewmodels.PetLostViewModel
import java.io.File

class PetLost : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = PetLost()
    }

    var imageHelper = ImageHelper()
    lateinit var takePic: Button
    lateinit var choosePic: Button
    lateinit var imageUpload: ImageView
    var inst: Fragment = this

    lateinit var petName: TextView
    lateinit var petType: Spinner

    lateinit var petSex: RadioGroup
    lateinit var petSize: RadioGroup

    lateinit var nextButton: Button

    lateinit var v: View
    private lateinit var rootLayout: ConstraintLayout

    private val viewModel: PetLostViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_lost_1_fragment, container, false)
        nextButton = v.findViewById(R.id.btnNext)
        rootLayout = v.findViewById(R.id.pet_lost_root_layout_1)
        petSex = v.findViewById(R.id.sex_radio)
        petSize = v.findViewById(R.id.size_radio)
        petName = v.findViewById(R.id.petName)
        petName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPetName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        petType = v.findViewById(R.id.pet_types_spinner)
        petType.setSelection(0, false)
        petType.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            v.context, R.array.pet_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            petType.adapter = adapter
        }

        (v.findViewById(R.id.radio_big_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_medium_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_small_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_mini_pet) as RadioButton).setOnClickListener(this)

        (v.findViewById(R.id.radio_male) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_female) as RadioButton).setOnClickListener(this)
        takePic = v.findViewById(R.id.takePic)
        choosePic = v.findViewById(R.id.choosePic)
        imageUpload = v.findViewById(R.id.imageUpload)
        takePic.setOnClickListener {
            imageHelper.takePicture(inst)
        }
        choosePic.setOnClickListener {
            imageHelper.choosePicture(inst)
        }
        viewModel.photo.observe(viewLifecycleOwner, {
            imageUpload.setImageURI(it)
            Glide.with(v.context).load(it).into(imageUpload)
        })

        nextButton.setOnClickListener {
            if (viewModel.validateStep1()) {
                val action = PetLostDirections.actionPetLostToPetLost2()
                v.findNavController().navigate(action)
            } else {
                Snackbar.make(rootLayout, "* Campos obligatorios", Snackbar.LENGTH_SHORT).show()
            }
        }

        return v
    }


    override fun onClick(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_big_pet,
                R.id.radio_medium_pet,
                R.id.radio_small_pet,
                R.id.radio_mini_pet ->
                    if (checked) {
                        viewModel.setPetSize(view.text.toString())
                    }
                R.id.radio_male,
                R.id.radio_female ->
                    if (checked) {
                        viewModel.setPetSex(view.text.toString())
                    }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.setPetType(petType.selectedItem.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}


    override fun onStart() {
        super.onStart()
        var lostPetRequest: LostPetRequest? = PetLostArgs.fromBundle(requireArguments()).petRequest
        if (lostPetRequest != null) fillData(lostPetRequest)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearALl()
    }

    private fun fillData(request: LostPetRequest) {
        viewModel.setRequest(request)
        petName.text = viewModel.petName.value
        viewModel.setRadioButton(petSize, viewModel.petSize)
        viewModel.setRadioButton(petSex, viewModel.petSex)
        viewModel.setSpinner(viewModel.petType, R.array.pet_types, v.context, petType)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        when (requestCode) {
            0 ->
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.setPhoto(File(imageHelper.currentPhotoPath).toUri())
                    imageUpload.setImageURI(viewModel.photo.value);
                }
            1 ->
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.setPhoto(data?.data!!)
                    imageUpload.setImageURI(viewModel.photo.value);
                }
        }
    }

}