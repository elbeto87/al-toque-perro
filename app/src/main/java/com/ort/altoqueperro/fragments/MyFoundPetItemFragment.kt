package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.LostPetRequest

class MyFoundPetItemFragment : Fragment() {
    lateinit var v: View
    private lateinit var myFoundPetImage: ImageView
    private lateinit var myFoundPetEdit: ImageView
    private lateinit var myFoundPetType: TextView
    private lateinit var myFoundPetSize: TextView
    private lateinit var myFoundPetSex: TextView
    private lateinit var myFoundPetCoat: TextView
    private lateinit var myFoundPetEyeColor: TextView
    private lateinit var similarPetName: TextView
    private lateinit var similarPetImage: ImageView
    private lateinit var similarPetType: TextView
    private lateinit var similarPetSize: TextView
    private lateinit var similarPetSex: TextView
    private lateinit var similarPetCoat: TextView
    private lateinit var similarPetEyeColor: TextView
    private lateinit var similarPetCardView: CardView
    private lateinit var foundPetData: FoundPetRequest
    private var similarPetData: LostPetRequest? = null
    private lateinit var txtConsulta: TextView

    companion object {
        fun newInstance() = MyFoundPetItemFragment()
    }

    private lateinit var viewModel: MyFoundPetItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.my_found_pet_item_fragment, container, false)

        myFoundPetImage = v.findViewById(R.id.imageMyFoundPetDetail)
        myFoundPetEdit = v.findViewById(R.id.imageMyFoundPetEdit)
        myFoundPetType = v.findViewById(R.id.txtMyFoundPetType)
        myFoundPetSize = v.findViewById(R.id.txtMyFoundPetSize)
        myFoundPetSex = v.findViewById(R.id.txtMyFoundPetSex)
        myFoundPetCoat = v.findViewById(R.id.txtMyFoundPetCoat)
        myFoundPetEyeColor = v.findViewById(R.id.txtMyFoundPetEyeColor)
        similarPetName = v.findViewById(R.id.txtSimilarLostPetName)
        similarPetImage = v.findViewById(R.id.imageSimilarLostPetDetail)
        similarPetType = v.findViewById(R.id.txtSimilarLostPetType)
        similarPetSize = v.findViewById(R.id.txtSimilarLostPetSize)
        similarPetSex = v.findViewById(R.id.txtSimilarLostPetSex)
        similarPetCoat = v.findViewById(R.id.txtSimilarLostPetCoat)
        similarPetEyeColor = v.findViewById(R.id.txtSimilarLostPetEyeColor)
        similarPetCardView = v.findViewById(R.id.cardItemSimilarLostPet)
        txtConsulta = v.findViewById(R.id.textViewMensaje)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyFoundPetItemViewModel::class.java)
        foundPetData = MyFoundPetItemFragmentArgs.fromBundle(requireArguments()).petData
        if (foundPetData.requestConsumer == null) {
            similarPetCardView.visibility = View.GONE
            txtConsulta.visibility = View.GONE
        } else {
            myFoundPetEdit.visibility = View.GONE
        }

        myFoundPetEdit.setOnClickListener {
            goToEdit()
        }

        viewModel.requestConsumerLiveData.observe(viewLifecycleOwner, {
            similarPetData = it
            similarPetName.text = it.pet.name
            similarPetType.text = it.pet.type
            similarPetSize.text = it.pet.size
            similarPetSex.text = it.pet.sex
            similarPetCoat.text = it.pet.furColor
            similarPetEyeColor.text = it.pet.eyes
            Glide.with(view.context).load(it.imageURL).into(similarPetImage)
        })
        myFoundPetType.text = foundPetData.pet.type
        myFoundPetSize.text = foundPetData.pet.size
        myFoundPetSex.text = foundPetData.pet.sex
        myFoundPetCoat.text = foundPetData.pet.furColor
        myFoundPetEyeColor.text = foundPetData.pet.eyes
        Glide.with(view.context).load(foundPetData.imageURL).into(myFoundPetImage)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getRequestConsumer(foundPetData.requestConsumer)
    }

    private fun goToEdit() {
        val action = MyFoundPetItemFragmentDirections.actionMyFoundPetItemFragmentToPetFound()
        action.petRequest = foundPetData
        v.findNavController().navigate(action)
    }

}