package com.ort.altoqueperro.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ort.altoqueperro.R
import com.ort.altoqueperro.utils.Notifications
import com.ort.altoqueperro.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {

    //ToDo revisar estos warnings
    lateinit var switchPetFound: Switch
    lateinit var switchPetLost: Switch
    private lateinit var switchNotification: Switch
    lateinit var v: View

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.settings_fragment, container, false)
        switchPetFound = v.findViewById(R.id.switchPetFound)
        switchPetLost = v.findViewById(R.id.switchPetLost)
        switchNotification = v.findViewById(R.id.switchNotifications)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        switchPetLost.isChecked = Notifications.getNotificationPetLost()
        switchPetFound.isChecked = Notifications.getNotificationPetFound()

        switchNotification.isChecked = switchPetLost.isChecked && switchPetFound.isChecked


        switchPetLost.setOnClickListener {
            refreshSwitchPetLost()

        }

        switchPetFound.setOnClickListener {
            refreshSwitchPetFound()
        }

        switchNotification.setOnClickListener {
            switchPetLost.isChecked = switchNotification.isChecked
            switchPetFound.isChecked = switchNotification.isChecked
            refreshSwitchPetLost()
            refreshSwitchPetFound()
        }

    }

    private fun refreshSwitchPetLost() {
        Notifications.setNotificationPetLost(switchPetLost.isChecked)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean("petLost", switchPetLost.isChecked)
            commit()
        }
    }

    private fun refreshSwitchPetFound() {
        Notifications.setNotificationPetFound(switchPetFound.isChecked)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean("petFound", switchPetFound.isChecked)
            commit()
        }
    }

}