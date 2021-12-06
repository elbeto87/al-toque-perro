package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.utils.ServiceLocation
import com.ort.altoqueperro.viewmodels.RescueCenterItemViewModel
import kotlin.math.roundToInt

class VetItemFragment : Fragment() {
    lateinit var v: View
    private lateinit var neighborhood: TextView
    lateinit var distance: TextView
    lateinit var name: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var businessHours: TextView
    private lateinit var imageLogo: ImageView
    private lateinit var address: TextView

    companion object {
        fun newInstance() = VetItemFragment()
    }

    private lateinit var viewModel: RescueCenterItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.vet_item_fragment, container, false)
        address = v.findViewById(R.id.textDirection)
        name = v.findViewById(R.id.txtVetName)
        phoneNumber = v.findViewById(R.id.txtVetPhoneNumber)
        businessHours = v.findViewById(R.id.txtVetBusinessHours)
        imageLogo = v.findViewById(R.id.txtVetLogo)
        neighborhood = v.findViewById(R.id.txtShelterLocation)
        distance = v.findViewById(R.id.txtDistance)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RescueCenterItemViewModel::class.java)
        val vetData: Vet = VetItemFragmentArgs.fromBundle(requireArguments()).vetData

        println(vetData.coordinates?.latitude.toString() + " " + vetData.coordinates?.longitude.toString())
        var distanceAct = 0
        if (vetData.coordinates != null) {
            distanceAct = ServiceLocation.getDistance(vetData.coordinates!!).roundToInt()
        }
        distance.text = "$distanceAct mts."
        address.text = vetData.address
        name.text = vetData.name
        neighborhood.text = vetData.localidad
        phoneNumber.text = vetData.phone
        businessHours.text = "Horacio de Atención ${vetData.businessHours}"
        Glide.with(view.context).load(vetData.imageUrl).into(imageLogo)

    }

}