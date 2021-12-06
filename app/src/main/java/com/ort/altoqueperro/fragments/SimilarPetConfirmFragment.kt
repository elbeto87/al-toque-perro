package com.ort.altoqueperro.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ort.altoqueperro.R
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.ort.altoqueperro.entities.User
import com.ort.altoqueperro.viewmodels.SimilarPetConfirmViewModel
import java.lang.Exception
import kotlin.String as String


class SimilarPetConfirmFragment : Fragment() {
    private lateinit var btnMessage : Button
    lateinit var v: View
    private lateinit var userId: String
    lateinit var user: User

    companion object {
        fun newInstance() = SimilarPetConfirmFragment()
    }

    private lateinit var viewModel: SimilarPetConfirmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v =  inflater.inflate(R.layout.similar_pet_confirm_fragment, container, false)
        btnMessage = v.findViewById(R.id.btnMensagge)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SimilarPetConfirmViewModel::class.java)
        userId = SimilarPetConfirmFragmentArgs.fromBundle(requireArguments()).userId
        viewModel.getUser(userId)

        viewModel.userLiveData.observe(viewLifecycleOwner, {
            user = it
        })

        btnMessage.setOnClickListener {
            sendWhatsApp()
        }

    }

    fun sendWhatsApp() {
        try {
            val text = "Hola! ${user.name} Te contacto gracias a Al Toque Perro, me parece que encontraste a mi mascota!"
            val toNumber = user.phone
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}