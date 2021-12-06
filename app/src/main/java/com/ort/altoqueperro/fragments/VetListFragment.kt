package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.altoqueperro.R
import com.ort.altoqueperro.adapter.VetListAdapter
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.viewmodels.VetListViewModel

class VetListFragment : Fragment() {
    lateinit var v: View
    private lateinit var recVet: RecyclerView

    companion object {
        fun newInstance() = VetListFragment()
    }

    private lateinit var listViewModel: VetListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.vet_list_fragment, container, false)
        recVet = v.findViewById(R.id.recylcer_view_vetList)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProvider(this).get(VetListViewModel::class.java)
        listViewModel.vetsLiveData.observe(viewLifecycleOwner, {

            recVet.adapter = VetListAdapter(listViewModel.vetsLiveData.value!!) { onVetClick(it) }
        })
    }

    override fun onStart() {
        super.onStart()
        recVet.setHasFixedSize(true)
        recVet.layoutManager = LinearLayoutManager(context)
        listViewModel.getVets()
    }

    private fun onVetClick(vet: Vet) {
        val action =
            ListModeDirections.actionListModeToVetItemFragment(vet)
        v.findNavController().navigate(action)
    }

}