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
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.viewmodels.PetLostViewModel

class PetLostConfirmation : Fragment() {

    companion object {
        fun newInstance() = PetLostConfirmation()
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
    private lateinit var imagen: ImageView
    lateinit var v: View

    private val viewModel: PetLostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_lost_confirmation_fragment, container, false)

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
        imagen = v.findViewById(R.id.imagen)

        viewModel.comments.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                txtCommentsValue.text = "Sin comentarios"
            } else {
                txtCommentsValue.text = it
            }
        })

        viewModel.petEyeColor.observe(viewLifecycleOwner, {
            txtPetEyeColorValue.text = it
        })
        viewModel.photo.observe(viewLifecycleOwner, {
            imagen.setImageURI(it)
            Glide.with(v.context).load(it).into(imagen)
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
            txtFoundTitle.text = "¡$it perdido!"
            txtPetTypeValue.text = it
        })

        viewModel.lostDate.observe(viewLifecycleOwner, {
            txtDateValue.text = it
        })



        return v
    }

    override fun onStart() {
        super.onStart()
        nextButton.setOnClickListener {
            val petRequest =
                viewModel.registerPet() //ToDo a revisar esto porque si da error se rompe la app creo
            lookForSimilarities(petRequest)
        }
    }

    private fun lookForSimilarities(petRequest: LostPetRequest) {
        val action =
            PetLostConfirmationDirections.actionPetLostConfirmationToPetLostSearchSimilarities(
                petRequest
            )
        v.findNavController().navigate(action)
    }


}