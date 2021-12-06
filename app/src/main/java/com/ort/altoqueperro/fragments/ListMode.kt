package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.ListModeViewModel

class ListMode : Fragment() {
    lateinit var v: View
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    companion object {
        fun newInstance() = ListMode()
    }

    private lateinit var viewModel: ListModeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.list_mode_fragment, container, false)
        tabLayout = v.findViewById(R.id.tab_layout)
        viewPager = v.findViewById(R.id.view_pager)
        return v
    }

    override fun onStart() {
        super.onStart()

        viewPager.adapter = ViewPagerAdapter(requireActivity())
        // viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Mascotas Perdidas"
                1 -> tab.text = "Veterinarias"
                2 -> tab.text = "Hogares de Transito"
                else -> tab.text = "undefined"
            }
        }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.postOnAnimation {
            tabLayout.getTabAt(ListModeArgs.fromBundle(requireArguments()).position)!!.select()
        }
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> LostPetListFragment()
                1 -> VetListFragment()
                2 -> ShelterListFragment()

                else -> LostPetListFragment()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object {
            private const val TAB_COUNT = 3
        }
    }
}