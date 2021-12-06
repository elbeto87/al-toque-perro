package com.ort.altoqueperro.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
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
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.utils.ImageHelper
import com.ort.altoqueperro.viewmodels.PetFoundViewModel
import java.io.File


class PetFound : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = PetFound()
    }

    private lateinit var petTypesSpinner: Spinner
    private lateinit var petSex: RadioGroup
    lateinit var petSize: RadioGroup

    private var imageHelper = ImageHelper()
    private lateinit var takePic: Button
    private lateinit var choosePic: Button
    private lateinit var imageUpload: ImageView
    private lateinit var nextButton: Button
    lateinit var v: View
    private var inst: Fragment = this
    private lateinit var rootLayout: ConstraintLayout

    private val viewModel: PetFoundViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_found_fragment, container, false)

        takePic = v.findViewById(R.id.takePic)
        choosePic = v.findViewById(R.id.choosePic)
        imageUpload = v.findViewById(R.id.imageUpload)

        nextButton = v.findViewById(R.id.btnNext)
        petSex = v.findViewById(R.id.sex_radio)
        petSize = v.findViewById(R.id.size_radio)

        rootLayout = v.findViewById(R.id.pet_found_root_layout_1)

        petTypesSpinner = v.findViewById(R.id.pet_types_spinner)
        petTypesSpinner.setSelection(0, false)
        petTypesSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            v.context, R.array.pet_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            petTypesSpinner.adapter = adapter
        }

        (v.findViewById(R.id.radio_big_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_medium_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_small_pet) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_mini_pet) as RadioButton).setOnClickListener(this)

        (v.findViewById(R.id.radio_male) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_female) as RadioButton).setOnClickListener(this)
        (v.findViewById(R.id.radio_dont_know) as RadioButton).setOnClickListener(this)

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
                val action = PetFoundDirections.actionPetFoundToPetFound2()
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
                R.id.radio_female,
                R.id.radio_dont_know ->
                    if (checked) {
                        viewModel.setPetSex(view.text.toString())
                    }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.setPetType(petTypesSpinner.selectedItem.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onStart() {
        super.onStart()
        val foundPetRequest = PetFoundArgs.fromBundle(requireArguments()).petRequest
        if (foundPetRequest != null) fillData(foundPetRequest)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearALl()
    }

    private fun fillData(request: FoundPetRequest) {
        viewModel.setRequest(request)
        viewModel.setRadioButton(petSize, viewModel.petSize)
        viewModel.setRadioButton(petSex, viewModel.petSex)
        viewModel.setSpinner(viewModel.petType, R.array.pet_types, v.context, petTypesSpinner)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 ->
                if (resultCode == RESULT_OK) {
                    viewModel.setPhoto(File(imageHelper.currentPhotoPath).toUri())
                    imageUpload.setImageURI(viewModel.photo.value)
                }
            1 ->
                if (resultCode == RESULT_OK) {
                    viewModel.setPhoto(data?.data!!)
                    imageUpload.setImageURI(viewModel.photo.value)
                }
        }
    }
}