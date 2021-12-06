package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.PetFoundHostYesViewModel

class PetFoundHostYes : Fragment() {

    companion object {
        fun newInstance() = PetFoundHostYes()
    }

    private lateinit var viewModel: PetFoundHostYesViewModel
    private lateinit var v: View
    private lateinit var backToMenu: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_found_host_yes_fragment, container, false)
        backToMenu = v.findViewById(R.id.backToMenu2)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetFoundHostYesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        backToMenu.setOnClickListener { backToMenu() }
    }

    private fun backToMenu() {
        val action = PetFoundHostYesDirections.actionPetFoundHostYesToNewMapModeFragment()
        v.findNavController().navigate(action)
    }

}