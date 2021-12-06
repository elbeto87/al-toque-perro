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
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.utils.ServiceLocation
import com.ort.altoqueperro.viewmodels.RescueCenterItemViewModel
import kotlin.math.roundToInt

class ShelterItemFragment : Fragment() {
    lateinit var v: View
    private lateinit var shelterName: TextView
    private lateinit var shelterAddress: TextView
    private lateinit var shelterPhoneNumber: TextView
    private lateinit var shelterImage: ImageView
    private lateinit var shelterLocation: TextView
    lateinit var distance: TextView

    companion object {
        fun newInstance() = ShelterItemFragment()
    }

    private lateinit var viewModel: RescueCenterItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.shelter_item_fragment, container, false)
        shelterAddress = v.findViewById(R.id.txtShelterAddress)
        shelterName = v.findViewById(R.id.txtShelterName)
        shelterPhoneNumber = v.findViewById(R.id.txtShelterPhoneNumber)
        shelterImage = v.findViewById(R.id.imageShelterDetail)
        shelterLocation = v.findViewById(R.id.txtShelterLocation)
        distance = v.findViewById(R.id.txtDistance)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RescueCenterItemViewModel::class.java)
        val shelterData: Shelter =
            ShelterItemFragmentArgs.fromBundle(requireArguments()).shelterData

        println("${shelterData.coordinates?.latitude.toString()} ${shelterData.coordinates?.longitude.toString()}")
        var distanceAct = 0
        if (shelterData.coordinates != null) {
            distanceAct = ServiceLocation.getDistance(shelterData.coordinates!!).roundToInt()
        }

        distance.text = "$distanceAct mts."
        shelterAddress.text = shelterData.address
        shelterName.text = shelterData.name
        shelterLocation.text = shelterData.localidad
        shelterPhoneNumber.text = shelterData.phoneNumber
        Glide.with(view.context).load(shelterData.imageUrl).into(shelterImage)

    }

}