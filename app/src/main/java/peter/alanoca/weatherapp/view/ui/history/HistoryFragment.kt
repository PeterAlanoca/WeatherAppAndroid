package peter.alanoca.weatherapp.view.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import peter.alanoca.weatherapp.R
import peter.alanoca.weatherapp.databinding.FragmentHistoryBinding
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.repository.datasource.Resource
import peter.alanoca.weatherapp.utility.extension.hideDialog
import peter.alanoca.weatherapp.utility.extension.showBottomSheet
import peter.alanoca.weatherapp.utility.extension.showDialog
import peter.alanoca.weatherapp.view.adapter.PlaceAdapter
import peter.alanoca.weatherapp.view.bottomsheet.WeatherBottomSheet
import peter.alanoca.weatherapp.view.dialog.LoadingDialog
import peter.alanoca.weatherapp.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        getPlaces()
        return binding.root
    }

    private fun getPlaces() {
        viewModel.getPlaces()
            .observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> { }
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { data ->
                            val placeAdapter = PlaceAdapter(data)
                            placeAdapter.onSelected { place ->
                                getCurrentData(place)
                            }
                            binding.episodesRecyclerView.adapter = placeAdapter
                        }
                    }
                    Resource.Status.ERROR -> {
                        resource.message?.let { Log.e(TAG, it) }
                    }
                }
            }
    }

    private fun getCurrentData(place: Place) {
        viewModel.getCurrentData(place.latitude, place.longitude)
            .observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        showDialog(LoadingDialog.newInstance())
                    }
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { data ->
                            hideDialog()
                            showBottomSheet(WeatherBottomSheet.newInstance(data, place))
                        }
                    }
                    Resource.Status.ERROR -> {
                        resource.message?.let { Log.e(TAG, it) }
                    }
                }
            }
    }

    companion object {
        private const val TAG = "HistoryFragment"
        fun newInstance() = HistoryFragment()
    }
}