package besmart.elderlycare.screen.gps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemListUserBinding
import besmart.elderlycare.model.profile.ProfileResponce

class GPSListUserAdapter(private val list: List<ProfileResponce>) :
    RecyclerView.Adapter<GPSListUserAdapter.GPSListUserHolder>() {

    private lateinit var binding: ItemListUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GPSListUserHolder {
        binding = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GPSListUserHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GPSListUserHolder, position: Int) {
        holder.bind(list[position])
    }

    class GPSListUserHolder(private val binding: ItemListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileResponce) {
            binding.model = profile
            binding.executePendingBindings()
        }
    }
}