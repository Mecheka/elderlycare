package besmart.elderlycare.screen.sugarhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemSugarHistoryBinding
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.util.convertDate

class SugarHistoryAdapter(private val list: MutableList<SugarResponse>) :
    RecyclerView.Adapter<SugarHistoryAdapter.SugarHistoryHolder>() {

    private lateinit var binding: ItemSugarHistoryBinding

    fun getItemByPosition(position: Int): SugarResponse {
        return list[position]
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugarHistoryHolder {
        binding =
            ItemSugarHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SugarHistoryHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SugarHistoryHolder, position: Int) {
        holder.bind(list[position])
    }

    class SugarHistoryHolder(private val binding: ItemSugarHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blood: SugarResponse) {
            binding.bmiLayout.setBackgroundColor(blood.getColor())
            binding.textFbsResult.text = blood.fbs.toString()
            binding.textTitle.text = blood.getResult()
            binding.textDate.text = blood.createAt?.convertDate()
        }
    }
}