package peter.alanoca.weatherapp.view.ui.weather

import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import peter.alanoca.weatherapp.R
import peter.alanoca.weatherapp.databinding.FragmentWeatherBinding
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import peter.alanoca.weatherapp.model.repository.datasource.Resource
import peter.alanoca.weatherapp.utility.extension.hideDialog
import peter.alanoca.weatherapp.utility.extension.showBottomSheet
import peter.alanoca.weatherapp.utility.extension.showDialog
import peter.alanoca.weatherapp.view.bottomsheet.WeatherBottomSheet
import peter.alanoca.weatherapp.view.dialog.LoadingDialog
import peter.alanoca.weatherapp.viewmodel.WeatherViewModel
import java.io.IOException
import com.google.android.libraries.places.api.model.Place as GPlace

class WeatherFragment : Fragment(R.layout.fragment_weather), OnMapReadyCallback {

    private val viewModel: WeatherViewModel by activityViewModels()

    private var binding: FragmentWeatherBinding? = null
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    private var currentLocation = LatLng(-9.770351, -74.8698974)
    private var zoomMin = 5.4f
    private var zoomMax = 14.4f
    private var place: Place? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentWeatherBinding.inflate(inflater, container, false)
            setupAutocomplete()
            setupMap(savedInstanceState)
        }
        return binding?.root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap?.let { map ->
            val cameraPosition =
                CameraPosition.Builder().target(currentLocation).zoom(zoomMin).build()
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            map.setOnMapClickListener { location ->
                generatePlace(location)
            }
        }
    }

    private fun addMarker(location: LatLng) {
        googleMap?.let { map ->
            this.currentLocation = location
            val cameraPosition = CameraPosition.Builder().target(location).zoom(zoomMax).build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            if (marker != null) {
                marker?.position = location
            } else {
                val markerOptions = MarkerOptions().position(location)
                marker = map.addMarker(markerOptions)
            }
            getCurrentData(location)
        }
    }

    private fun setupAutocomplete() {
        try {
            binding?.let { b ->
                if (!Places.isInitialized()) {
                    Places.initialize(requireContext(), getString(R.string.google_maps_key))
                }
                val fields: List<GPlace.Field> = listOf(
                    GPlace.Field.ID,
                    GPlace.Field.NAME,
                    GPlace.Field.ADDRESS,
                    GPlace.Field.LAT_LNG
                )
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(requireContext())
                b.searchView.setOnClickListener {
                    startAutocomplete.launch(intent);
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        try {
            binding?.let { b ->
                b.mapView.onCreate(savedInstanceState)
                b.mapView.onResume()
                MapsInitializer.initialize(requireContext())
                b.mapView.getMapAsync(this)
            }
        } catch (e: Exception) {
            //Log.e("MAPS", e.message)
        }
    }

    private fun generatePlace(location: LatLng) {
        try {
            this.currentLocation = location
            val places =
                Geocoder(requireContext()).getFromLocation(location.latitude, location.longitude, 1)
            if (places.isNotEmpty()) {
                val selectedPlace = places.firstOrNull()
                selectedPlace?.let {
                    val address = selectedPlace.getAddressLine(0)
                    address?.let {
                        addMarker(location)
                        val selectedAddress = it.lines().firstOrNull() ?: "SN"
                        place = Place(
                            0,
                            location.latitude,
                            location.longitude,
                            selectedPlace.countryName,
                            selectedAddress
                        )
                    }
                }
            }
        } catch (_: IOException) {
        }
    }

    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        (ActivityResultCallback { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    place.latLng?.let { generatePlace(it) }
                }
            }
        })
    )

    private fun getCurrentData(location: LatLng) {
        viewModel.getCurrentData(location.latitude, location.longitude)
            .observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        showDialog(LoadingDialog.newInstance())
                    }
                    Resource.Status.SUCCESS -> {
                        place?.let { p ->
                            resource.data?.let { data ->
                                hideDialog()
                                showButtonSheet(data, p)
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        resource.message?.let { Log.e(TAG, it) }
                    }
                }
            }
    }

    private fun insertPlace(place: Place) {
        viewModel.insertPlace(place)
            .observe(this) { }
    }

    private fun showButtonSheet(weatherResponse: WeatherResponse, place: Place) {
        insertPlace(place)
        showBottomSheet(WeatherBottomSheet.newInstance(weatherResponse, place))
    }

    companion object {
        private const val TAG = "WeatherFragment"
        fun newInstance() = WeatherFragment()
    }
}

