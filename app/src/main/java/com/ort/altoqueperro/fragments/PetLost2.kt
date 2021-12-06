package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.PetLostViewModel

class PetLost2 : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = PetLost2()
    }

    private lateinit var nextButton: Button
    private lateinit var petEyeColorSpinner: Spinner
    private lateinit var petFurColorSpinner: Spinner
    private lateinit var petFurLengthSpinner: Spinner
    private lateinit var petNosesSpinner: Spinner

    lateinit var v: View
    private lateinit var rootLayout: ConstraintLayout
    private val viewModel: PetLostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_lost_2_fragment, container, false)

        nextButton = v.findViewById(R.id.btnNext)

        rootLayout = v.findViewById(R.id.pet_lost_root_layout_2)

        petEyeColorSpinner = v.findViewById(R.id.pet_eye_color_spinner)
        petFurColorSpinner = v.findViewById(R.id.pet_fur_color_spinner)
        petFurLengthSpinner = v.findViewById(R.id.pet_fur_length_spinner)
        petNosesSpinner = v.findViewById(R.id.pet_nose_spinner)

        configureSpinner(petEyeColorSpinner, R.array.pet_eye_colors)
        configureSpinner(petFurColorSpinner, R.array.pet_fur_colors)
        configureSpinner(petFurLengthSpinner, R.array.pet_fur_lengths)
        configureSpinner(petNosesSpinner, R.array.pet_noses)

        nextButton.setOnClickListener {
            if (viewModel.validateStep2()) {
                val action = PetLost2Directions.actionPetLost2ToPetLost3()
                v.findNavController().navigate(action)
            } else {
                Snackbar.make(rootLayout, "* Campos obligatorios", Snackbar.LENGTH_SHORT).show()

            }
        }

        return v
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.id) {
            petEyeColorSpinner.id -> viewModel.setPetEyeColor(petEyeColorSpinner.selectedItem.toString())
            petFurColorSpinner.id -> viewModel.setPetFurColor(petFurColorSpinner.selectedItem.toString())
            petFurLengthSpinner.id -> viewModel.setPetFurLength(petFurLengthSpinner.selectedItem.toString())
            petNosesSpinner.id -> viewModel.setPetNose(petNosesSpinner.selectedItem.toString())
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onResume() {
        super.onResume()
        fillData()
    }

    private fun fillData() {
        viewModel.setSpinner(viewModel.petNose, R.array.pet_noses, v.context, petNosesSpinner)
        viewModel.setSpinner(
            viewModel.petFurLength,
            R.array.pet_fur_lengths,
            v.context,
            petFurLengthSpinner
        )
        viewModel.setSpinner(
            viewModel.petFurColor,
            R.array.pet_fur_colors,
            v.context,
            petFurColorSpinner
        )
        viewModel.setSpinner(
            viewModel.petEyeColor,
            R.array.pet_eye_colors,
            v.context,
            petEyeColorSpinner
        )
    }

    private fun configureSpinner(spinner: Spinner, textArrayResId: Int) {
        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            v.context, textArrayResId, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


}