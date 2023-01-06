package peter.alanoca.weatherapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import peter.alanoca.weatherapp.databinding.ItemPlaceBinding
import peter.alanoca.weatherapp.model.entity.Place

class PlaceAdapter(private val episodes: List<Place> = emptyList()) : RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {

    private var action: ((Place) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        holder.set(episodes[position])
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    inner class PlaceHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun set(place: Place) {
            binding.addressTextView.text = place.address
            itemView.setOnClickListener {
                action?.let { action ->
                    action(place)
                }
            }
        }
    }

    fun onSelected(action: (Place) -> Unit) {
        this.action = action
    }
}
