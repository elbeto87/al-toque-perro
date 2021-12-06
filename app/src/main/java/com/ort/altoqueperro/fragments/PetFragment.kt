package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.PetViewModel

class PetFragment : Fragment() {

    companion object {
        fun newInstance() = PetFragment()
    }

    private lateinit var welcomeText: TextView
    lateinit var v: View
    private lateinit var foundPet: Button
    private lateinit var lostPet: Button
    private lateinit var viewModel: PetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_fragment, container, false)

        welcomeText = v.findViewById(R.id.newMapModeText)
        foundPet = v.findViewById(R.id.foundPetBtn)
        lostPet = v.findViewById(R.id.lostPetBtn)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetViewModel::class.java)
        viewModel.userLiveData.observe(viewLifecycleOwner, {
            welcomeText.text = "Bienvenido ${it.name} !"
        })
    }

    override fun onStart() {
        super.onStart()

        viewModel.getUser(Firebase.auth.currentUser?.uid.toString())

        foundPet.setOnClickListener {
            foundPet()
        }

        lostPet.setOnClickListener {
            lostPet()
        }
    }

    private fun foundPet() {
        val action = PetFragmentDirections.actionPetFragment2ToPetFound()
        v.findNavController().navigate(action)
    }

    private fun lostPet() {
        val action = PetFragmentDirections.actionPetFragment2ToPetLost()
        v.findNavController().navigate(action)
    }

}