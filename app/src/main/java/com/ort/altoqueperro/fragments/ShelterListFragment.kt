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
import com.ort.altoqueperro.adapter.ShelterListAdapter
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.viewmodels.ShelterListViewModel

class ShelterListFragment : Fragment() {
    lateinit var v: View
    private lateinit var recShelters: RecyclerView

    companion object {
        fun newInstance() = ShelterListFragment()
    }

    private lateinit var listViewModel: ShelterListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.shelter_list_fragment, container, false)
        recShelters = v.findViewById(R.id.recylcer_view_shelterList)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProvider(this).get(ShelterListViewModel::class.java)
        listViewModel.sheltersLiveData.observe(viewLifecycleOwner, {

            recShelters.adapter =
                ShelterListAdapter(listViewModel.sheltersLiveData.value!!) { onShelterClick(it) }
        })
    }

    override fun onStart() {
        super.onStart()
        recShelters.setHasFixedSize(true)
        recShelters.layoutManager = LinearLayoutManager(context)
        listViewModel.getShelters()
    }

    private fun onShelterClick(shelter: Shelter) {
        val action =
            ListModeDirections.actionListModeToShelterItemFragment(shelter)
        v.findNavController().navigate(action)
    }

}