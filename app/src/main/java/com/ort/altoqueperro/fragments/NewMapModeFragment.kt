package com.ort.altoqueperro.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.LostPetRequest
import com.ort.altoqueperro.entities.Shelter
import com.ort.altoqueperro.entities.Vet
import com.ort.altoqueperro.utils.Notifications
import com.ort.altoqueperro.utils.PermissionUtils.isPermissionGranted
import com.ort.altoqueperro.utils.PermissionUtils.requestPermission
import com.ort.altoqueperro.utils.ServiceLocation
import com.ort.altoqueperro.utils.ServiceLocationGet
import com.ort.altoqueperro.viewmodels.NewMapModeViewModel
import kotlin.math.roundToInt


class NewMapModeFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var map: GoogleMap
    private var permissionDenied = false
    private lateinit var serviceLocationGet: ServiceLocationGet
    private var firstRun = true
    //var inst: Fragment = this
    //var idUserLogged = FirebaseAuth.getInstance().currentUser?.uid.toString()
    //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

    companion object {
        fun newInstance() = NewMapModeFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var viewModel: NewMapModeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.new_map_mode_fragment, container, false)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        serviceLocationGet = ServiceLocationGet(requireActivity()) {
            if (firstRun) {
                firstRun = false
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            ServiceLocation.location.latitude,
                            ServiceLocation.location.longitude
                        ), 17.0f
                    )
                )
            } else {
                println("Se actualiza la posicion")
            }
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewMapModeViewModel::class.java)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val idUserLogged = FirebaseAuth.getInstance().currentUser?.uid.toString()
        map = googleMap
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        map.setInfoWindowAdapter(CustomInfoWindowAdapter())
        enableMyLocation()
        viewModel.getLostPets()
        map.clear()
        viewModel.requestRepository.observe(this, {
            viewModel.filterRequests().forEach {
                if (it.coordinates != null) {
                    val bmp: BitmapDescriptor = if (it.requestCreator == idUserLogged) {
                        BitmapDescriptorFactory.fromResource(R.drawable.ownpaw)
                    } else {
                        BitmapDescriptorFactory.fromResource(R.drawable.dow_paw)
                    }
                    val marker: MarkerOptions =
                        MarkerOptions().position(it.coordinates!!.getLatLng()).title(it.pet.name)
                            .icon(
                                bmp
                            )
                    map.addMarker(marker)?.tag = it
                }
            }
        })

        viewModel.getShelters()
        viewModel.sheltersRepository.observe(this, { it ->
            it.forEach {
                if (it.coordinates != null) {
                    val bmp: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.home)
                    val marker: MarkerOptions =
                        MarkerOptions().position(it.coordinates!!.getLatLng()).title(it.name).icon(
                            bmp
                        )
                    map.addMarker(marker)?.tag = it
                }
            }
        })

        viewModel.getVets()
        viewModel.vetsRepository.observe(this, { it ->
            it.forEach {
                if (it.coordinates != null) {
                    val bmp: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.vetmapa)
                    val marker: MarkerOptions =
                        MarkerOptions().position(it.coordinates!!.getLatLng()).title(it.name).icon(
                            bmp
                        )
                    map.addMarker(marker)?.tag = it
                }
            }
        })
        map.setOnInfoWindowClickListener {
            if (it.tag is LostPetRequest) {
                val pet: LostPetRequest = it.tag as LostPetRequest
                if (pet.requestCreator == idUserLogged) {
                    onMyLostPetClick(pet)
                } else {
                    onLostPetClick(pet)
                }
            }
            if (it.tag is Vet) {
                val vet: Vet = it.tag as Vet
                onVetClick(vet)
            }
            if (it.tag is Shelter) {
                val shelter: Shelter = it.tag as Shelter
                onShelterClick(shelter)
            }
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val booleanListPetLost = sharedPref.getBoolean("petLost", false)
        val booleanListPetFound = sharedPref.getBoolean("petFound", false)

        Notifications.setNotificationPetLost(booleanListPetLost)

        Notifications.setNotificationPetFound(booleanListPetFound)
    }

    private fun gotoMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this.requireContext(), "Permiso de acceso denegado", Toast.LENGTH_LONG)
                .show()
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(-34.6090638, -58.4289158),
                    17.0f
                )
            )
            return
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                this.activity as AppCompatActivity, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        gotoMyLocation()
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
    }

    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            Toast.makeText(
                requireContext(),
                "No permitio que sepamos donde esta asi que el mapa aparecera por cualquier lado",
                Toast.LENGTH_LONG
            ).show()
            permissionDenied = true
            // [END_EXCLUDE]
        }
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        /** These can be lateinit as they are set in onCreate */

        // These are both view groups containing an ImageView with id "badge" and two
        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View {
            // This means that getInfoContents will be called.
            marker.setInfoWindowAnchor(-1.1f, 0f)
            render(marker, window)
            return window
        }

        override fun getInfoContents(marker: Marker): View {
            render(marker, contents)
            return contents
        }

        //ToDo no entiendo por que se pasa el view si no se usa
        private fun render(marker: Marker, view: View) {
            //val badge = R.drawable.dog // Passing 0 to setImageResource will clear the image view.
            val distance = window.findViewById<TextView>(R.id.distance)
            val size = window.findViewById<TextView>(R.id.size)
            val coatColor = window.findViewById<TextView>(R.id.coatColor)
            //val more = window.findViewById<TextView>(R.id.more)
            val imagen = window.findViewById<ImageView>(R.id.badge)
            if (marker.tag is LostPetRequest) {
                val pet: LostPetRequest = marker.tag as LostPetRequest
                size.text = "Tamaño: ${pet.pet.size}"
                coatColor.text = "Color: ${pet.pet.furColor}"
                distance.text =
                    "${ServiceLocation.getDistance(pet.coordinates!!).roundToInt()} mts."
                //imagen.setImageResource(imagen)
                Glide.with(view).load(pet.imageURL).into(imagen)
            }
            if (marker.tag is Vet) {
                val vet: Vet = marker.tag as Vet
                size.text = "Nombre: ${vet.name}"
                coatColor.text = "Horario: ${vet.businessHours}"
                distance.text =
                    "${ServiceLocation.getDistance(vet.coordinates!!).roundToInt()} mts."
                //imagen.setImageResource(R.drawable.vetmapa)
                Glide.with(view).load(vet.imageUrl).into(imagen)
            }
            if (marker.tag is Shelter) {
                val shelter: Shelter = marker.tag as Shelter
                size.text = "Nombre: ${shelter.name}"
                coatColor.text = "Telefono: ${shelter.phoneNumber}"
                distance.text =
                    "${ServiceLocation.getDistance(shelter.coordinates!!).roundToInt()} mts."
                //imagen.setImageResource(R.drawable.home)
                Glide.with(view).load(shelter.imageUrl).into(imagen)
            }
        }
    }

    private fun onLostPetClick(lostPet: LostPetRequest) {
        val action =
            NewMapModeFragmentDirections.actionNewMapModeFragmentToLostPetItemFragment(lostPet)
        this.parentFragment?.findNavController()?.navigate(action)
    }

    private fun onMyLostPetClick(lostPet: LostPetRequest) {
        val action =
            NewMapModeFragmentDirections.actionNewMapModeFragmentToMyLostPetItemFragment(lostPet)
        this.parentFragment?.findNavController()?.navigate(action)
    }

    private fun onVetClick(vet: Vet) {
        val action =
            NewMapModeFragmentDirections.actionNewMapModeFragmentToVetItemFragment(vet)
        this.parentFragment?.findNavController()?.navigate(action)
    }

    private fun onShelterClick(shelter: Shelter) {
        val action =
            NewMapModeFragmentDirections.actionNewMapModeFragmentToShelterItemFragment(shelter)
        this.parentFragment?.findNavController()?.navigate(action)
    }
}

