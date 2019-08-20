package besmart.elderlycare.screen.villagehealthvoluntor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ItemListUserBinding
import besmart.elderlycare.databinding.ItemVillageTitleBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.villageitem.VillageItem
import besmart.elderlycare.model.villageitem.VillageType
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageUrlCircle
import kotlinx.android.synthetic.main.item_village_head.view.*

class VillageHealthVolumteerAdapter(private val list: MutableList<VillageItem>, private val listener: SimpleOnItemClick<ProfileResponce>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VillageType.TILTE -> {
                TitleViewHolder(ItemVillageTitleBinding.inflate(layoutInflater, parent, false))
            }
            VillageType.HEAD -> {
                HeadViewHolder(layoutInflater.inflate(R.layout.item_village_head, parent, false))
            }
            else -> {
                ListViewHolder(ItemListUserBinding.inflate(layoutInflater, parent, false), listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].type) {
            VillageType.TILTE -> {
                holder as TitleViewHolder
                holder.bind(list[position].content as ProfileResponce)
            }
            VillageType.HEAD -> {
                holder as HeadViewHolder
                holder.bind(list[position].content as String)
            }
            else -> {
                holder as ListViewHolder
                holder.bind(list[position].content as ProfileResponce)
            }
        }
    }

    class TitleViewHolder(private val binding: ItemVillageTitleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileResponce) {
            binding.model = profile
            binding.executePendingBindings()
            binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL + "/" + profile.imagePath)
        }
    }

    class HeadViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String) {
            view.text_Header.text = text
        }
    }

    class ListViewHolder(private val binding: ItemListUserBinding, private val listener: SimpleOnItemClick<ProfileResponce>) : RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileResponce) {
            binding.model = profile
            binding.executePendingBindings()
            binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL + "/" + profile.imagePath)
            binding.rootLayout.setOnClickListener {
                listener.onItemClick(profile)
            }
        }
    }
}
