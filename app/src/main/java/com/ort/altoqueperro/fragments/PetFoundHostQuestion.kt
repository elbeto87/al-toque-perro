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
import com.ort.altoqueperro.viewmodels.PetFoundHostQuestionViewModel

class PetFoundHostQuestion : Fragment() {

    companion object {
        fun newInstance() = PetFoundHostQuestion()
    }

    private lateinit var viewModel: PetFoundHostQuestionViewModel
    private lateinit var yesHost: Button
    private lateinit var noHost: Button
    private lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_found_host_question_fragment, container, false)
        yesHost = v.findViewById(R.id.yesHost)
        noHost = v.findViewById(R.id.noHost)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetFoundHostQuestionViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        yesHost.setOnClickListener { yesHost() }
        noHost.setOnClickListener { noHost() }
    }

    private fun yesHost() {
        val action = PetFoundHostQuestionDirections.actionPetFoundHostQuestionToPetFoundHostYes()
        v.findNavController().navigate(action)
    }

    private fun noHost() {
        val action = PetFoundHostQuestionDirections.actionPetFoundHostQuestionToPetFoundHostNo()
        v.findNavController().navigate(action)
    }

}