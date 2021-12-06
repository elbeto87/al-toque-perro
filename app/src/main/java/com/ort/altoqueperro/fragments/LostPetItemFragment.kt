package com.ort.altoqueperro.fragments

import android.content.Intent
import android.net.Uri
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
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.viewmodels.LostPetItemViewModel

class LostPetItemFragment : Fragment() {
    lateinit var v: View
    private lateinit var lostPetImage: ImageView
    private lateinit var lostPetName: TextView
    private lateinit var lostPetType: TextView
    private lateinit var lostPetSize: TextView
    private lateinit var lostPetSex: TextView
    private lateinit var lostPetEyeColor: TextView
    private lateinit var lostPetNose: TextView
    private lateinit var lostPetFurLength: TextView
    private lateinit var lostPetFurColor: TextView
    private lateinit var lostPetComments: TextView
    private lateinit var lostPetLostDate: TextView
    private lateinit var btnContactWhatsapp: TextView
    private lateinit var lostPetData: LostPetRequest
    private lateinit var userId: String
    private lateinit var user: User

    companion object {
        fun newInstance() = LostPetItemFragment()
    }

    private lateinit var viewModel: LostPetItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_item_fragment, container, false)
        lostPetImage = v.findViewById(R.id.imagePetDetail)
        lostPetName = v.findViewById(R.id.txtPetName)
        lostPetType = v.findViewById(R.id.txtPetType)
        lostPetSize = v.findViewById(R.id.txtPetSize)
        lostPetSex = v.findViewById(R.id.txtPetSex)
        lostPetEyeColor = v.findViewById(R.id.txtPetEyeColor)
        lostPetFurLength = v.findViewById(R.id.txtPetFurLength)
        lostPetNose = v.findViewById(R.id.txtPetNose)
        lostPetFurColor = v.findViewById(R.id.txtPetFurColor)
        lostPetComments = v.findViewById(R.id.txtPetComments)
        lostPetLostDate = v.findViewById(R.id.txtPetLostDate)
        btnContactWhatsapp = v.findViewById(R.id.btnWP)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LostPetItemViewModel::class.java)
        userId =
            LostPetItemFragmentArgs.fromBundle(requireArguments()).petData?.requestCreator.toString()
        lostPetData = LostPetItemFragmentArgs.fromBundle(requireArguments()).petData!!
        lostPetName.text = lostPetData.pet.name
        lostPetType.text = lostPetData.pet.type
        lostPetSize.text = lostPetData.pet.size
        lostPetSex.text = lostPetData.pet.sex
        lostPetEyeColor.text = lostPetData.pet.eyes
        lostPetFurLength.text = lostPetData.pet.furLength
        lostPetFurColor.text = lostPetData.pet.furColor
        lostPetComments.text = lostPetData.pet.comments
        lostPetLostDate.text = lostPetData.pet.lostDate
        Glide.with(view).load(lostPetData.imageURL).into(lostPetImage)

        viewModel.userLiveData.observe(viewLifecycleOwner, {
            user = it
        })

        btnContactWhatsapp.setOnClickListener {
            sendWhatsApp()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser(userId)
    }

    private fun sendWhatsApp() {
        try {
            val text =
                "Hola! ${user.name} Te contacto gracias a Al Toque Perro, me parece que encontraste a mi mascota!"
            val toNumber = user.phone
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
