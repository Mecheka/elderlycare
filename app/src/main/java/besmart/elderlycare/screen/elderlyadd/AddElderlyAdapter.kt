package besmart.elderlycare.screen.elderlyadd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ItemAddElderlyBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.loadImageResourceCircle
import besmart.elderlycare.util.loadImageUrlCircle

class AddElderlyAdapter(private val list: MutableList<ProfileResponce>, private val listener: OnSelectItemListenner) :
    RecyclerView.Adapter<AddElderlyAdapter.AddElderlyHolder>(), AddItemListenner {

    private lateinit var binding: ItemAddElderlyBinding
    private var selectPostion: Int? = null

    fun getItemSelect(){
        selectPostion?.let {
            listener.onSelect(list[it])
        }?:run{
            listener.onError()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddElderlyHolder {
        binding = ItemAddElderlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddElderlyHolder(binding,this)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holderAdd: AddElderlyHolder, position: Int) {
        holderAdd.bind(list[position])
        selectPostion?.let {
            if (position == it){
                holderAdd.setSeleciItem()
            }else{
                holderAdd.hideItem()
            }
        }
    }

    override fun onClick(adapterPosition: Int) {
        selectPostion = adapterPosition
        notifyDataSetChanged()
    }

    class AddElderlyHolder(private val binding: ItemAddElderlyBinding, private val listener: AddItemListenner) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileResponce) {
            binding.model = profile
            profile.imagePath?.let {
                binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL +"/"+ it)
            }?:run{
                binding.imageProfile.loadImageResourceCircle(R.drawable.baseline_person_24px)
            }
            binding.executePendingBindings()
            binding.rootLayout.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }

        fun setSeleciItem(){
            binding.imageDone.visibility = View.VISIBLE
        }

        fun hideItem(){
            binding.imageDone.visibility = View.GONE
        }
    }


}

interface AddItemListenner{
    fun onClick(adapterPosition: Int)
}

interface OnSelectItemListenner{
    fun onSelect(profileResponce: ProfileResponce)
    fun onError()
}