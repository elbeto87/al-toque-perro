package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.entities.PetRequest
import com.ort.altoqueperro.entities.RequestScore
import com.ort.altoqueperro.viewmodels.SimilarPetViewModel
import org.w3c.dom.Text

class SimilarPetFragment : Fragment() {
    lateinit var v: View
    private lateinit var petImage: ImageView
    lateinit var titleFragment: TextView
    lateinit var petType: TextView
    lateinit var petSize: TextView
    private lateinit var petSex: TextView
    //private lateinit var petCoat: TextView
    private lateinit var petNose : TextView
    private lateinit var petEyeColor: TextView
    private lateinit var petFur: TextView
    private lateinit var petFurColor: TextView
    private lateinit var dateLost: TextView
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button
    lateinit var lostPetRequest: LostPetRequest
    lateinit var requestScore: RequestScore
    private lateinit var txtComments:TextView

    companion object {
        fun newInstance() = SimilarPetFragment()
    }

    private lateinit var viewModel: SimilarPetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.similar_pet_fragment, container, false)
        titleFragment = v.findViewById(R.id.titleFragment)
        petImage = v.findViewById(R.id.petImage)
        petType = v.findViewById(R.id.txtSimilarPetType)
        petSex = v.findViewById(R.id.txtSimilarPetSex)
        petSize = v.findViewById(R.id.txtSimilarPetSize)
        petNose = v.findViewById(R.id.txtPetNoseValue)
        petFur = v.findViewById(R.id.txtPetFurLengthValue)
        petFurColor = v.findViewById(R.id.txtPetFurColorValue)
        petEyeColor = v.findViewById(R.id.txtSimilarPetEyeColor)
        //petCoat = v.findViewById(R.id.txtSimilarPetCoat)
        dateLost = v.findViewById(R.id.txtPetDateValue)
        txtComments = v.findViewById(R.id.txtCommentsValue)
        confirmButton = v.findViewById(R.id.btnSimilarPetConfirm)
        cancelButton = v.findViewById(R.id.btnSimilarPetCancel)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SimilarPetViewModel::class.java)
        requestScore = SimilarPetFragmentArgs.fromBundle(requireArguments()).requestScore
        lostPetRequest = SimilarPetFragmentArgs.fromBundle(requireArguments()).petRequest

        val foundPetRequest: PetRequest = requestScore.request!!

        Glide.with(view.context).load(foundPetRequest.imageURL).into(petImage)
        petType.text = foundPetRequest.pet.type
        petSex.text = foundPetRequest.pet.sex
        petSize.text = foundPetRequest.pet.size
        petNose.text = foundPetRequest.pet.nose
        petFur.text = foundPetRequest.pet.furLength
        petFurColor.text = foundPetRequest.pet.furColor
        petEyeColor.text = foundPetRequest.pet.eyes
        dateLost.text = foundPetRequest.pet.lostDate
        txtComments.text = foundPetRequest.pet.comments

        if (!lostPetRequest.isOpen()) {
            cancelButton.visibility = View.GONE
            confirmButton.visibility = View.GONE
        }

        confirmButton.setOnClickListener {
            lostPetRequest.nextStateConfirm(foundPetRequest)
            saveChanges()
            goToUserData()
        }

        cancelButton.setOnClickListener {
            backToSimilarPetList()
        }

    }

    private fun backToSimilarPetList() {
        v.findNavController().popBackStack()
    }

    private fun goToUserData() {
        val action = SimilarPetFragmentDirections.actionSimilarPetFragmentToSimilarPetConfirm(
            requestScore.request!!.requestCreator
        )
        v.findNavController().navigate(action)
    }

    private fun saveChanges() {
        viewModel.updateRequests(requestScore.request, lostPetRequest)
    }

}