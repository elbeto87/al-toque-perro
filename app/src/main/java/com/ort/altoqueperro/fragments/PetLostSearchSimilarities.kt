package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.altoqueperro.R
import com.ort.altoqueperro.adapter.SimilarPetsAdapter
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.entities.RequestScore
import com.ort.altoqueperro.viewmodels.PetLostSearchSimilaritiesViewModel

class PetLostSearchSimilarities : Fragment() {

    companion object {
        fun newInstance() = PetLostSearchSimilarities()
    }

    private lateinit var viewModel: PetLostSearchSimilaritiesViewModel
    private lateinit var recSimilarPets: RecyclerView
    private lateinit var noResult: Button
    private lateinit var petRequestData: LostPetRequest
    private lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_lost_search_similarities_fragment, container, false)
        noResult = v.findViewById(R.id.noResults)
        recSimilarPets = v.findViewById(R.id.recicler_view_search_for_similarities)

        return v

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetLostSearchSimilaritiesViewModel::class.java)
        petRequestData = PetLostSearchSimilaritiesArgs.fromBundle(requireArguments()).petRequest
        viewModel.getComparingScore()
        viewModel.getFoundPetRequests()
        viewModel.comparingScoreLiveData.observe(viewLifecycleOwner, {
            viewModel.foundPetRequestRepository.observe(viewLifecycleOwner, {
                val similarRequests = viewModel.lookForSimilarities(petRequestData)
                if (similarRequests.isEmpty() && it.isNotEmpty()) noResult()
                recSimilarPets.adapter =
                    SimilarPetsAdapter(similarRequests) { requestScore ->
                        onSimilarPetsClick(
                            requestScore
                        )
                    }

            })
        })
    }

    override fun onStart() {
        super.onStart()

        noResult.setOnClickListener { noResult() }
        recSimilarPets.setHasFixedSize(true)
        recSimilarPets.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun onSimilarPetsClick(requestScore: RequestScore) {
        val action =
            PetLostSearchSimilaritiesDirections.actionPetLostSearchSimilaritiesToSimilarPetFragment(
                requestScore,
                petRequestData
            )
        v.findNavController().navigate(action)
    }

    private fun noResult() {
        val action =
            PetLostSearchSimilaritiesDirections.actionPetLostSearchSimilaritiesToPetLostFinalMessage()
        v.findNavController().navigate(action)
    }
}