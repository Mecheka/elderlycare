package besmart.elderlycare.screen.knowledge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemKnowlegeBinding
import besmart.elderlycare.model.knowlege.KnowlegeResponce
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageUrl

class KnowlegeAdapter(
    private val list: List<KnowlegeResponce>,
    private val onItemClick: SimpleOnItemClick<KnowlegeResponce>
) :
    RecyclerView.Adapter<KnowlegeAdapter.KnowlegeHolder>() {

    private lateinit var binding: ItemKnowlegeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowlegeHolder {
        binding = ItemKnowlegeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnowlegeHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: KnowlegeHolder, position: Int) {
        holder.bind(list[position])
    }

    class KnowlegeHolder(
        private val binding: ItemKnowlegeBinding,
        private val onItemClick: SimpleOnItemClick<KnowlegeResponce>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(knowlege: KnowlegeResponce) {
            binding.model = knowlege
            binding.imageKnowlege.loadImageUrl(Constance.DEVMAN_URL + knowlege.thumbnail)
            binding.executePendingBindings()
            binding.layoutRoot.setOnClickListener {
                onItemClick.onItemClick(knowlege)
            }
        }
    }
}