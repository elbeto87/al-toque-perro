package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.PetFoundViewModel

class PetFoundConfirmation : Fragment() {

    companion object {
        fun newInstance() = PetFoundConfirmation()
    }

    private lateinit var nextButton: Button
    private lateinit var txtCommentsValue: TextView
    private lateinit var txtDateValue: TextView
    private lateinit var txtFoundTitle: TextView
    private lateinit var txtPetEyeColorValue: TextView
    private lateinit var txtPetFurColorValue: TextView
    private lateinit var txtPetFurLengthValue: TextView
    private lateinit var txtPetNoseValue: TextView
    private lateinit var txtPetSexValue: TextView
    private lateinit var txtPetSizeValue: TextView
    private lateinit var txtPetTypeValue: TextView
    private lateinit var photo: ImageView
    lateinit var v: View

    private val viewModel: PetFoundViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_found_confirmation_fragment, container, false)

        nextButton = v.findViewById(R.id.btnPublish)

        txtCommentsValue = v.findViewById(R.id.txtCommentsValue)
        txtDateValue = v.findViewById(R.id.txtPetDateValue)
        txtFoundTitle = v.findViewById(R.id.txtFoundTitle)
        txtPetEyeColorValue = v.findViewById(R.id.txtSimilarPetEyeColor)
        txtPetFurColorValue = v.findViewById(R.id.txtPetFurColorValue)
        txtPetFurLengthValue = v.findViewById(R.id.txtPetFurLengthValue)
        txtPetNoseValue = v.findViewById(R.id.txtPetNoseValue)
        txtPetSexValue = v.findViewById(R.id.txtSimilarPetSex)
        txtPetSizeValue = v.findViewById(R.id.txtSimilarPetSize)
        txtPetTypeValue = v.findViewById(R.id.txtSimilarPetType)
        photo = v.findViewById(R.id.photo)

        viewModel.comments.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                txtCommentsValue.text = "Sin comentarios"
            } else {
                txtCommentsValue.text = it

            }
        })

        viewModel.petEyeColor.observe(viewLifecycleOwner, {
            txtPetEyeColorValue.text = it
        })

        viewModel.petFurColor.observe(viewLifecycleOwner, {
            txtPetFurColorValue.text = it
        })

        viewModel.petFurLength.observe(viewLifecycleOwner, {
            txtPetFurLengthValue.text = it
        })

        viewModel.petNose.observe(viewLifecycleOwner, {
            txtPetNoseValue.text = it
        })

        viewModel.petSex.observe(viewLifecycleOwner, {
            txtPetSexValue.text = it
        })

        viewModel.petSize.observe(viewLifecycleOwner, {
            txtPetSizeValue.text = it
        })

        viewModel.petType.observe(viewLifecycleOwner, {
            txtFoundTitle.text = "¡$it encontrado!"
            txtPetTypeValue.text = it
        })

        viewModel.lostDate.observe(viewLifecycleOwner, {
            txtDateValue.text = it
        })
        viewModel.photo.observe(viewLifecycleOwner, {
            photo.setImageURI(it)
            Glide.with(v.context).load(it).into(photo)
        })

        return v
    }

    override fun onStart() {
        super.onStart()

        nextButton.setOnClickListener {
            viewModel.registerPet()
            navigateNext()
        }
    }

    private fun navigateNext() {
        val action =
            PetFoundConfirmationDirections.actionPetFoundConfirmationToPetFoundHostQuestion()
        v.findNavController().navigate(action)
    }


}
