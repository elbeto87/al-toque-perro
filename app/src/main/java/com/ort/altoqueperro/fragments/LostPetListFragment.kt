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
import com.ort.altoqueperro.adapter.MyFoundPetAdapter
import com.ort.altoqueperro.adapter.MyPetAdapter
import com.ort.altoqueperro.adapter.PetAdapter
import com.ort.altoqueperro.entities.FoundPetRequest
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.utils.Notifications
import com.ort.altoqueperro.viewmodels.LostPetListViewModel

class LostPetListFragment : Fragment() {
    lateinit var v: View
    private lateinit var recOwnLostPets: RecyclerView
    private lateinit var recOwnFoundPets: RecyclerView
    private lateinit var recLostPets: RecyclerView

    companion object {
        fun newInstance() = LostPetListFragment()
    }

    private lateinit var listViewModel: LostPetListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_list_fragment, container, false)
        recLostPets = v.findViewById(R.id.recylcer_view_petList)
        recOwnLostPets = v.findViewById(R.id.recylcer_view_ownLostPetList)
        recOwnFoundPets = v.findViewById(R.id.recylcer_view_ownFoundPetList)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProvider(this).get(LostPetListViewModel::class.java)
        listViewModel.requestRepository.observe(viewLifecycleOwner, {
            listViewModel.filterRequests()
            recLostPets.adapter =
                PetAdapter(listViewModel.othersLostRequestsRepository) { onLostPetClick(it) }
            recOwnLostPets.adapter =
                MyPetAdapter(listViewModel.myLostRequestsRepository) { onMyLostPetClick(it) }

        })
        listViewModel.foundRequestsRepository.observe(viewLifecycleOwner, {
            recOwnFoundPets.adapter =
                MyFoundPetAdapter(listViewModel.foundRequestsRepository.value!!) {
                    onMyFoundPetClick(
                        it
                    )
                }
        })
    }

    override fun onResume() {
        super.onResume()
        println(Notifications.getNotificationPetLost())
        recLostPets.setHasFixedSize(false)
        recLostPets.layoutManager = LinearLayoutManager(context)
        recOwnLostPets.setHasFixedSize(false)
        recOwnLostPets.layoutManager = LinearLayoutManager(context)
        recOwnFoundPets.setHasFixedSize(false)
        recOwnFoundPets.layoutManager = LinearLayoutManager(context)
        listViewModel.getLostPets()
        if (!Notifications.getNotificationPetFound()) {
            listViewModel.getOwnFoundPets()
        }
    }

    private fun onLostPetClick(lostPet: LostPetRequest) {
        val action =
            ListModeDirections.actionListModeToLostPetItemFragment(lostPet)
        v.findNavController().navigate(action)
    }

    private fun onMyLostPetClick(lostPet: LostPetRequest) {
        val action =
            ListModeDirections.actionListModeToMyLostPetItemFragment(lostPet)
        v.findNavController().navigate(action)
    }

    private fun onMyFoundPetClick(foundPet: FoundPetRequest) {
        val action =
            ListModeDirections.actionListModeToMyFoundPetItemFragment(foundPet)
        v.findNavController().navigate(action)
    }

}