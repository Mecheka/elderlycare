package besmart.elderlycare.screen.myelderlyprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ItemMyElderlyBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageResource
import besmart.elderlycare.util.loadImageUrl

class MyElderlyProfileAdapter(private val list: List<ProfileResponce>, private val listener:SimpleOnItemClick<ProfileResponce>) :
    RecyclerView.Adapter<MyElderlyProfileAdapter.ElderlyHolder>() {

    private lateinit var binding: ItemMyElderlyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElderlyHolder {
        binding = ItemMyElderlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ElderlyHolder(binding,listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ElderlyHolder, position: Int) {
        holder.bind(list[position])
    }

    class ElderlyHolder(private val binding: ItemMyElderlyBinding,private val listener: SimpleOnItemClick<ProfileResponce>) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileResponce) {
            binding.model = profile
            profile.imagePath?.let {
                binding.imageProfile.loadImageUrl(Constance.BASE_URL+"/"+it)
            }?:run{
                binding.imageProfile.loadImageResource(R.drawable.baseline_person_24px)
            }
            binding.executePendingBindings()
            binding.cardView.setOnClickListener {
                listener.onItemClick(profile)
            }
        }
    }
}